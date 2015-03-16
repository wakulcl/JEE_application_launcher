//Miguel Álvarez Úbeda ha enviado este código a la lista de desarrolladores de gvSIG:

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ApplicationLauncher {

public static void main(String args[]){
	if( args.length!=1) {
		System.out.println("Numero de parametros erroneo forma de uso: java -cp . ApplicationLauncher fichero|URL");
	return;
	}
	if(args[0].indexOf("http")!=-1) launchURL(args[0]);
	else launchDefaultViewer(args[0]);
}

private static String linuxDesktop = null;

private static String getEnv(String envvar){
	try{
		Process p = Runtime.getRuntime().exec("/bin/sh echo $"+envvar);
		BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String value = br.readLine();
		if(value==null) return "";
		else return value.trim ();
	}
	catch(Exception error){
		return "";
	}
}

private static String getLinuxDesktop(){
	//solo se averigua el entorno de escritorio una vez, despues se almacena en la variable estatica
	if(linuxDesktop!=null) return linuxDesktop;
	if(!getEnv("KDE_FULL_SESSION").equals("") || !getEnv("KDE_MULTIHEAD").equals("")){
		linuxDesktop="kde";
	}
	else if(!getEnv("GNOME_DESKTOP_SESSION_ID").equals("") || !getEnv("GNOME_KEYRING_SOCKET").equals("")){
		linuxDesktop="gnome";
	}
	else linuxDesktop="";
	
	return linuxDesktop;
}

public static Process launchURL(String url){
	try{
	if (System.getProperty("os.name").toUpperCase().indexOf("95") != -1)
		return Runtime.getRuntime().exec( new String[]{"command.com", "/C", "start", url} );
	if (System.getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1)
		return Runtime.getRuntime().exec( new String[]{"cmd.exe", "/C", "start", url} );
	if (System.getProperty("os.name").toUpperCase().indexOf("MAC") != -1)
		return Runtime.getRuntime().exec( new String[]{"open", url} );
	if (System.getProperty("os.name").toUpperCase().indexOf("LINUX") != -1 ) {
	if(getLinuxDesktop().equals("kde"))
		return Runtime.getRuntime().exec( new String[]{"kfmclient", "exec", url} );
	else
		return Runtime.getRuntime().exec( new String[]{"gnome-open", url} );
	}
}
catch(IOException ioex){System.out.println(ioex);}

return null;
}

public static Process launchDefaultViewer(String filepath){
	//return launchURL( new File(filepath).getAbsolutePath()); linea original
	return launchURL( filepath );
}

}