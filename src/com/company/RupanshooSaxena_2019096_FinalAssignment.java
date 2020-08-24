package com.company;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/* Name: Rupanshoo Saxena
 * Roll number: 2019096
 * Stream: CSE
 * Section: A
 * Computer Organization End Semester Final Assignment
 */

/* Assumptions:
        1. assuming a 32- bit memory system
        2. the physical address entered is a 32 bit binary address.
*/


public class RupanshooSaxena_2019096_FinalAssignment {  // the central class of the complete program

    static int counterAsss=1;  //for fully set associative
    static int[] counter = new int[10000]; //for k-way set associative

    static class CacheNode {
        String  index;
        int data;

        public CacheNode(String index, int data){
            this.index = index;
            this.data = data;
        }
    }

    static class TagNode{
        String ind;
        String msb;

        public TagNode(String ind, String msb){
            this.ind=ind;
            this.msb=msb;
        }
    }

    static class TagAsss{   //for Associative method
        String address;
        int data;

        public TagAsss(String address, int data){
            this.address = address;
            this.data = data;
        }
    }


    static int twosPower(int num){    //to convert a number to a power of 2 --> taking log
        int power = 0;
        while(num/2 >= 1){
            num = num/2;
            power++;
        }
        return power;
    }

    static String retCacheBits(String PhysicalAdd, int ta, int cac){ //returns bits of cache
        String cach = PhysicalAdd.substring(ta,(ta+cac));
        return cach;
    }

    static String retTagBits(String PhysicalAdd, int ta){  //returns bits of Tag
        String tag1 = PhysicalAdd.substring(0,ta);
        return tag1;
    }

    static String retOffsetBits(String PhysicalAdd, int cac, int ta){  //returns bits of offset
        String OFF = PhysicalAdd.substring((ta+cac));
        return OFF;
    }





    static void searchCacheDM(String address, CacheNode[] Arra, int ta, int ca, int noOfBlock, TagNode[] TArra){
        String phyCac = retCacheBits(address, ta, ca);
        String phyTag = retTagBits(address,ta);
        //printCache(Arra,noOfBlock);
        //printTag(TArra,noOfBlock);
        for(int w=0; w< noOfBlock; w++){
            if (Arra[w].index.equals(phyCac)){

                if(phyTag.equals(TArra[w].msb)){
                    System.out.println("Hit");
                }
                else
                    System.out.println("Miss");

            }



        }
    }

    static void searchCacheAss(String address, Queue<TagAsss> AsArr, int noOfBlock, int TagAssbits){
        Iterator<TagAsss> it = ((LinkedList<TagAsss>)AsArr).listIterator();
        boolean t=false;
        String addMSB = retTagBits(address,TagAssbits);
        while(it.hasNext()){
            TagAsss current = it.next();
            String currmsb = retTagBits(current.address,TagAssbits);
            if(addMSB.equals(currmsb)){
                t=true;
                System.out.println("Hit- found address in cache");
            }
        }
        if(!t){
            System.out.println("Miss");
        }

    }

    static void writeCacheDM(String address, int value, int ta, int ca, CacheNode[] arr, TagNode[] tArr, int noOfBlk){
        String addCac = retCacheBits(address, ta, ca);
        //System.out.println(addCac.getClass());
        //System.out.println(arr[1].index.getClass());
        //System.out.println(addCac);
        for(int q=0;q< noOfBlk; q++){
            if(addCac.equals(arr[q].index)){
                arr[q].data = value;
                tArr[q].msb = retTagBits(address,ta);
            }
        }
        System.out.println("Loaded into Cache");
    }


    static void writeCacheAsss(String address, int VAl, int ta, Queue<TagAsss> AsArr, int noOfBlock){
        String TagAsbits = retTagBits(address,ta);
        TagAsss disappointment = new TagAsss(TagAsbits,VAl);
        if(counterAsss<=noOfBlock){
            AsArr.add(disappointment);
        }
        else{
            AsArr.remove();
            AsArr.add(disappointment);
        }
        counterAsss++;
    }


    static void printCache(CacheNode[] cache, int size){
        for(int i=0;i<size;i++){
            System.out.println(cache[i].index + ":" + cache[i].data);
        }
    }

    static void printTag(TagNode[] tag, int size){
        for(int i=0;i<size;i++){
            System.out.println(tag[i].ind + ":" + tag[i].msb);
        }
    }

    static int setAddress(String PhysicalAdd, int offset, int setbits){
        int tagBi = 32-(offset+setbits);
        String SET = PhysicalAdd.substring(tagBi,tagBi+setbits);
        int decimalSET = Integer.parseInt(SET,2);
        return decimalSET+1;
    }



    static void writeCache3(String phys,int off,int setb,int k,Queue<CacheNode>[] Qarr, int val, TagNode[] tar){
        int s=setAddress(phys,off,setb);
        String msbBits = retTagBits(phys,32-(setb+off));
        CacheNode cnd=new CacheNode("",val);
        if (counter[s] < k) {

            Qarr[s].add(cnd);
            tar[s].msb = msbBits;
            counter[s]++;


        }else{
            Qarr[s].remove();
            Qarr[s].add(cnd);
            tar[s].msb = msbBits;

        }


    }

    static void search3(String phys,int off,int setb,Queue<CacheNode>[] Qarr, TagNode[] tarr){
        int s=setAddress(phys,off,setb);
        String msBBits = retTagBits(phys,32-(setb+off));

        for(int i=0;i<Qarr[s].size();i++){
            if(s == Integer.parseInt(tarr[s].ind)){
                if(msBBits.equals(tarr[s].msb)){
                    System.out.println("Hit");

                }
                else
                    System.out.println("Miss");

            }
        }

    }





    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);

        System.out.println("S- cache size(in bytes):");
        int S = scan.nextInt();  //cache size in Bytes

        System.out.println("B-block size (in bytes):");
        int B = scan.nextInt();  //block size in Bytes

        System.out.println("k (for k-way associated cache:");
        int k = scan.nextInt();  //k-way associated cache

        int Cl = S/B;  //cache lines


        int cacheIndexBits = twosPower(Cl);   //the number of bytes per cache index

        int offsetBits = twosPower(B);       // the number of bytes that will be assigned as offset

        int Tagbits = 32-(offsetBits+cacheIndexBits);          // the number of bytes assigned to Tag after subtracting offset from total bits.

        int TagAssBit = 32-offsetBits;             //tag for fully associative cache

        CacheNode[] cacheArray=new CacheNode[Cl];     //Array of CacheNode - For direct method


        for(int i=0;i<Cl;i++){                               //assigning binary indexes to blocks
            String binIndex = Integer.toBinaryString(i);    // indexes are made to the bits required by adding zeroes
            while(binIndex.length()<cacheIndexBits){
                binIndex = '0' + binIndex;
            }

            CacheNode newNode = new CacheNode(binIndex,-1);   //an object of CacheNode used to put initial values in cacheArray
            cacheArray[i]=newNode;

            /* we initialize data with -1. If data = -1, this shows that the node is empty.
             * We also initialize index with the binary bits (no. of cache index bits is determined by
             * the logarithm of no. of Cache lines.)
             */
        }



        TagNode[] TagArray = new TagNode[Cl];   //Tag Array for direct method


        for(int i=0;i<Cl;i++){                               //assigning binary indexes to blocks
            String binIndex = Integer.toBinaryString(i);    // indexes are made to the bits required by adding zeroes
            while(binIndex.length()<cacheIndexBits){
                binIndex = '0' + binIndex;
            }
            //System.out.println(binIndex);
            TagNode newTagNode = new TagNode(binIndex,"xx");
            TagArray[i]=newTagNode;

            /* we initialise msb bits of each clock by string "xx" which implies that the block is empty.
             * Also, the index is initialised with the binary bits.
             * There are Cl no. of blocks in the tag array, same as Cache array
             */
        }

        Queue<TagAsss> TagAs = new LinkedList<>();   // A queue of TagAsss nodes - for fully associative methods

        Queue<CacheNode>[] QArr;    // Array of queues - for k-way set associative method
        TagNode[] tar;              // Tag array for k-way set associative method

        int noOfSets = Cl/k;       // no. of sets in the cache array
        int bitSets = twosPower(noOfSets);    //no. of bits required to do indexing of the sets - by taking logarithm

        QArr = new LinkedList[k+1];    //defining the array of queues for the k-way set associative method
        for(int i=0;i<=k;i++){
            QArr[i]=new LinkedList<>();
        }

        tar = new TagNode[Cl];         //defining the tagnode array for k-way set associative method
        int checkk=1;                  // a counter to check that the number of sets in the tag Array
        // doesn't exceed the no. of sets in cache array as they must be the same

        while (checkk<=noOfSets){
            TagNode temp = new TagNode(Integer.toString(checkk), "xx");
            tar[checkk]=temp;
            checkk+=k-1;

            /*
            assigning a value of "xx" to all the empty tag nodes and doing their indexing by the set no.
            they belong to.

             */
        }


        while (true) {                                          //menu driven program begins

            System.out.println("Cache loading and searching\n");
            System.out.println("1. Direct Mapping\n");
            System.out.println("2. Associative memory\n");
            System.out.println("3. k-way set associative memory\n");
            System.out.println("4. Exit");

            int choice1 = scan.nextInt();   //to choose the type of mapping - enter the number

            switch (choice1) {
                case 1:   // direct mapping
                    System.out.println("1. Search for an address in Cache, check Hit or Miss\n");
                    System.out.println("2. Write data to Cache using a physical memory address\n");
                    int choice2 = scan.nextInt();     //to choose loading or searching

                    switch (choice2) {   //switch for loading or searching

                        case 1:   //searching
                            System.out.println("\nEnter the physical address of the memory location(32 bits binary, MSB --> LSB): ");
                            String phyAdd1 = scan.next();
                            searchCacheDM(phyAdd1, cacheArray, Tagbits, cacheIndexBits, Cl, TagArray);
                            //calling the search function using direct mapping

                            break;


                        case 2:   //loading
                            System.out.println("\nEnter the physical address of the memory location(32 bits binary, MSB --> LSB):");
                            String phyAdd2 = scan.next();

                            System.out.println("\nEnter the value to be written(integer): ");
                            int writeVal = scan.nextInt();

                            writeCacheDM(phyAdd2, writeVal, Tagbits, cacheIndexBits, cacheArray, TagArray, Cl);
                            //calling the loading function using direct mapping

                            //printCache(cacheArray,Cl);
                            //printTag(TagArray, Cl);
                            break;


                        default:                  // in case a wrong number choice is entered
                            System.out.println("\nwrong choice");
                            break;
                    }




                case 2:    //Associative memory
                    System.out.println("1. Search for an address in Cache, check Hit or Miss\n");
                    System.out.println("2. Write data to Cache using a physical memory address\n");
                    int choice3 = scan.nextInt();   //to enter choice
                    switch (choice3){   // switch for loading and searching

                        case 1:   //search
                            System.out.println("\nEnter the physical address of the memory location(32 bits binary, MSB --> LSB): ");
                            String phyAdd4 = scan.next();
                            searchCacheAss(phyAdd4,TagAs,Cl, TagAssBit);
                            //calling the search function using associative memory

                            break;


                        case 2:   //load
                            System.out.println("\nEnter the physical address of the memory location(32 bits binary, MSB --> LSB):");
                            String phyAdd3 = scan.next();
                            System.out.println("\nEnter the value to be written(integer): ");
                            int Val = scan.nextInt();
                            writeCacheAsss(phyAdd3,Val,TagAssBit, TagAs,Cl);
                            //calling the loading function using associative memory

                            //System.out.println("checkk!!");
                            break;


                        default:
                            System.out.println("wrong choice");  //wrong choice
                            break;
                    }
                    break;




                case 3:  //k-way set associative memory

                    System.out.println("1. Search for an address in Cache, check Hit or Miss\n");
                    System.out.println("2. Write data to Cache using a physical memory address\n");
                    int choice4 = scan.nextInt();

                    switch (choice4){
                        case 1:   //search
                            System.out.println("\nEnter the physical address of the memory location(32 bits binary, MSB --> LSB): ");
                            String phyAdd5 = scan.next();
                            search3(phyAdd5,offsetBits,bitSets,QArr,tar);
                            // calling the searching function using the k-way associative memory

                            break;


                        case 2:  //load
                            System.out.println("\nEnter the physical address of the memory location(32 bits binary, MSB --> LSB):");
                            String phyAdd6 = scan.next();
                            System.out.println("\nEnter the value to be written(integer): ");
                            int insertVAl = scan.nextInt();
                            writeCache3(phyAdd6,offsetBits,bitSets,k,QArr,insertVAl,tar);
                            //calling the loading function using the k-way associative memory

                            break;


                        default:
                            System.out.println("wrong choice");  //for wrong choice entered
                            break;

                    }
                    break;


                case 4:   //to exit from the cache menu
                    System.exit(0);


                default:   //wrong choice
                    System.out.println("\nwrong choice");
                    break;
            }
        }





    }
}
