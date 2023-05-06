
import java.io.*;
import java.util.*;

public class SopaDeLetras {

    public static String[] leerArchivo(File file) {//lectura del archivo inicial con las palabra separadas por comas 
        String[] vector = null; //crear e instaciar el vector donde seran guardadas las palaras
        try (BufferedReader obj = new BufferedReader(new FileReader(file))) {

            String linea;
            String[] datos = null;

            // lee cada línea del archivo y separa los datos por comas
            while ((linea = obj.readLine()) != null) {
                datos = linea.split(",");//dividir el string del archivo por comas

                // crea un vector y guarda cada dato en una posición
                vector = new String[datos.length];
                System.arraycopy(datos, 0, vector, 0, datos.length);

            }
            // Ordenar de mayor a menor longitud para asegurarse de poner las palabras mas grandes primero
            Arrays.sort(vector, Comparator.comparing(String::length).reversed());
        
            

        } catch (IOException e) { //si el archivo no existe error 
            System.out.println("Error al leer el archivo");
        }

        return vector; //el vector con las palabras ya 
    }

    

    public static char[][] posicionar(char[][] matriz, String[] vector) {//metodo para posicionar las palabras del vector en la matriz
        int n = matriz.length; //n representa la dimensión de la matriz
        String[] infoPosicion = new String[vector.length]; // Vector donde se almacena la informacion de posicionamiento de cada palabra 
        int posicionX; //variable de la posición incial en x
        int posicionY; //variable de la posición incial en x
        String info; //acá se concatena toda la información de posicionamiento
        int tamanoInsert; //si n es el tamaño de la palabra, esta variable sera n-1
        boolean isPosible; //almacena el resultado de analizar si es posible colocar la palabra en esa posición
        String palabra; // dato en vector[i]
        int k ; //movimiento en horizontal o vertical segun sea el caso
        int fin; //la casilla donde termina la palabra 

        for (int i = 0; i < vector.length; i++) { // recorre el vector
            boolean j = true; // para salir del while, cambia si la palabra ya se ha posicionado
            while (j) { //while para tratar de encontrar una posición las veces que sea necesario siemprey cuando j sea true
                Random random = new Random(); 
                posicionX = random.nextInt(n); //ramdom para la posicion inicial de x
                posicionY = random.nextInt(n); //ramdom para la posicion inicial de y

                palabra = vector[i]; 
                // busca horizontal
                tamanoInsert = palabra.length() - 1;
                fin = posicionY + tamanoInsert;

                if ((fin <= n - 1)) { //para evitar el desboramiento solo es posible colocar palabras si no exeden el tamaño de la matriz
                    isPosible = posible(posicionX, posicionY, 1, tamanoInsert, matriz); //evalua la posiilidad
                    if (isPosible) {
                        info = "palabra " + palabra + ",horizontal [" + posicionX + "," + posicionY + "hasta :" + fin + "]"; 
                        infoPosicion[i] = info;

                        // agregar la palabra a la matriz
                        k = posicionY; //si posiciono en horizontal debo moverme en vertical 
                        for (int p = 0; p < palabra.length(); p++) {// for para moverse caracter a caracter por la palabra
                            if (k <= fin) { // siempre y cuando la posicion inicial de y no supere su posicion final
                                matriz[posicionX][k] = palabra.charAt(p); //fija x y se desplaza en y y almacena el caracter 
                                k++;
                            }
                        }
                        j = false;
                    }
                }

                if (j == true) {//las posiciona vertical siempre y cuando haya fallado el intento horizontal
                    fin = posicionX + tamanoInsert; //esto es como arriba, lo unico que cambia es que se fija y y se desplaza en x
                    if (fin <= n - 1) {
                        isPosible = posible(posicionX, posicionY, 0, tamanoInsert, matriz);
                        if (isPosible) {
                            info = "palabra " + vector[i] + ",vertical [" + posicionX + "," + posicionY + " hasta: "
                                    + fin + "]";
                            infoPosicion[i] = info;

                            // agregar la palabra a la matriz
                            k = posicionX;
                            for (int p = 0; p < palabra.length(); p++) {
                                if (k <= posicionX + tamanoInsert) {
                                    matriz[k][posicionY] = palabra.charAt(p);
                                    k++;
                                }
                            }
                            j = false;

                        }
                    }

                }
            }
        }
        escribirArchivo(infoPosicion); //llamado a la funcion escribir archivo
        return matriz; //retorno de la matriz 
    }
        
    public static boolean posible(int iniciox, int inicioy, int direccion, int fina, char[][] matriz) {//evalua la posibilidad de polocar una palabra en un numero determinado de espacios en la matriz
        boolean isPosible = true;
        int i ;
        int p ;

        if (direccion == 1) {//horizontal
            p = inicioy+fina; //donde finalizaria la palabra 
            i = inicioy;
            while (i <= p) {//while siempre avanzar desde la posición inicial en y 
                if (matriz[iniciox][i] != '\0') { //solo se posiciona la palabra en ese espacio agrupado si las posiciones individuales estan vacias 
                    isPosible = false; //falso si al menos una posición tiene alguna letra de otra palabra
                }
                i++;
                
            }
        } 

        //lo mismo pero aplicado a vertical
        if (direccion == 0) { //vertical
            i = iniciox;
            p = iniciox+fina;
            while (i <= p) {
                if (matriz[i][inicioy] != '\0') {
                    isPosible = false;
                }
                i++;
            }
        }
        return isPosible; //retorna el resultado
    }

    public static void escribirArchivo(String[] infoPocision) { //escribir en un nuevo archivo la información de las palabras que se posicionaron 

        try {
            File archivo = new File("archivo.txt");//si el archivo no existe se crea
            if (!archivo.exists()) {
                archivo.createNewFile();
            }
            BufferedWriter writer = new BufferedWriter(new FileWriter(archivo));
            for (String registro : infoPocision) {
                writer.write(registro);
                writer.newLine(); // Salto de línea para cada registro
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void escribirSopaArchivo(char[][] matriz) {

        try {
            // Abrir archivo en modo append
            BufferedWriter writer = new BufferedWriter(new FileWriter("archivo.txt", true));
            BufferedReader reader = new BufferedReader(new FileReader("archivo.txt"));

            // Verificar si el archivo está vacío
            //esto para escribir bajo la información de las palabras ubicadas
            boolean empty = reader.readLine() == null;

            // Agregar salto de línea si no está vacío
            if (!empty) {
                writer.newLine();
            }

            // Escribir matriz en el archivo
            for (int i = 0; i < matriz.length; i++) {
                for (int j = 0; j < matriz[i].length; j++) {
                    writer.write(matriz[i][j]);
                    if (j < matriz[i].length - 1) {
                        writer.write(" "); // separar cada letra por un espacio
                    }
                }
                writer.newLine(); // Salto de línea al avanzar en la fila
            }
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    
    public static char[][] insertRandomLetras(char[][] matriz) {//llenar la matriz simpre que sea 0 o null la casilla
        Random rand = new Random();
        for (int i = 0; i < matriz.length; i++) {
            for (int j = 0; j < matriz[i].length; j++) {
                if (matriz[i][j] == 0 ) {
                    matriz[i][j] = (char) (rand.nextInt(26) + 'A');
                }
            }
        }
        
        escribirSopaArchivo(matriz);//llamado a la funcion escribirSopaArchivo
        return matriz; //retorno de matriz
    }


   /*public static void printMatrix(char[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    } */

   

    public static void main(String[] args) { //programa principal

        File file = new File("palabras.txt");//nombre del archivo y ubicación del archivo con las palabras
        String[] vector = leerArchivo(file); //uso de la funcion leer archivo

        //crear la matriz 
        Random rand = new Random();
        int tamMatriz = rand.nextInt((29 - 19) + 1) + 19; // Valor entre 19 y 29, ambos incluidos ya que es desde 0

        // crear matriz con n = tamMatriz
        char[][] sopa = new char[tamMatriz][tamMatriz];
        

        sopa = posicionar(sopa, vector);//usos de la funcion posicionar
        sopa = insertRandomLetras(sopa);// uso de la  funcion insertarRandomLetras
        //printMatrix(sopa);

    }
}
