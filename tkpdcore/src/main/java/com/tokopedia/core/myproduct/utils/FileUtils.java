package com.tokopedia.core.myproduct.utils;

import android.content.Context;
import android.util.Log;

import com.tokopedia.core.app.MainApplication;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Created by m.normansyah on 12/8/15.
 */
public class FileUtils {

    /**
     * example of result : /storage/emulated/0/Android/data/com.tokopedia.tkpd/1451274244/
     * @param root
     * @return
     */
    public static String getFolderPathForUpload(String root){
        return root+"/Android/data/"+ MainApplication.PACKAGE_NAME+"/"+(System.currentTimeMillis() / 1000L)+new Random().nextInt(1000) + "/";
    }

    /**
     * example of result : /storage/emulated/0/Android/data/com.tokopedia.tkpd/1451274244/image.jpg
     * @param root
     * @param output
     * @param extension
     * @return
     */
    public static String getPathForUpload(String root, String output, String extension){
        return root+"/Android/data/"+ MainApplication.PACKAGE_NAME+"/"+(System.currentTimeMillis() / 1000L) + "/"+output+"."+extension;
    }

    public static void writeStringAsFileExt(Context context, final String fileContents, String fileName) {
        try {
            File root = android.os.Environment.getExternalStorageDirectory();
            FileWriter out = new FileWriter(new File(root.getAbsolutePath())+"/"+ fileName);//new File(context.getExternalFilesDir(null)
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }
}