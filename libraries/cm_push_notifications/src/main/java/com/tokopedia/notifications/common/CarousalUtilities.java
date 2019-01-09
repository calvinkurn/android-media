package com.tokopedia.notifications.common;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.tokopedia.notifications.model.Carousal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by Ashwani Tyagi on 13/12/18.
 */
public class CarousalUtilities {

    static final String IMAGE_DIR = "imageDir";


    public static void downloadImages(Context context, List<Carousal> carousalList) {
        for (Carousal carousal : carousalList) {
            if (!TextUtils.isEmpty(carousal.getFilePath())) {
                continue;
            }
            Bitmap bitmap = CMNotificationUtils.loadBitmapFromUrl(carousal.getIcon());
            if (null != bitmap) {
                String path = carousalSaveBitmapToInternalStorage(context, bitmap, String.valueOf(System.currentTimeMillis()));
                carousal.setFilePath(path);
            }
        }

    }

    /**
     * @param context
     * @param bitmapImage
     * @param fileName
     * @return
     */
    public static String carousalSaveBitmapToInternalStorage(Context context, Bitmap bitmapImage, String fileName) {
        boolean fileSaved = false;
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory = cw.getDir(IMAGE_DIR, Context.MODE_PRIVATE);
        File imagePath = new File(directory, fileName + ".png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imagePath);
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fileSaved = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
            }
        }
        if (fileSaved)
            return imagePath.getAbsolutePath();
        else
            return null;
    }

    /**
     * @param path
     * @return
     */

    public static Bitmap carousalLoadImageFromStorage(String path) {
        Bitmap b = null;

        try {
            File f = new File(path);
            b = BitmapFactory.decodeStream(new FileInputStream(f));
        } catch (FileNotFoundException e) {
        }
        return b;
    }

    public static void deleteCarousalImageDirectory(Context context) {
        try {
            ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
            File dir = cw.getDir(IMAGE_DIR, Context.MODE_PRIVATE);
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        } catch (Exception e) {

        }
    }
}
