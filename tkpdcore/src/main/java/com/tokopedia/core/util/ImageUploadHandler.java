package com.tokopedia.core.util;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.myproduct.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Tkpd_Eka on 6/30/2015.
 */
public class ImageUploadHandler {

    private static final String TAG = ImageUploadHandler.class.getSimpleName();

    public static ImageUploadHandler createInstance(Fragment fragment) {
        ImageUploadHandler uploadimage = new ImageUploadHandler();
        return uploadimage;
    }
    public static ImageUploadHandler createInstance(androidx.fragment.app.Fragment fragment) {
        ImageUploadHandler uploadimage = new ImageUploadHandler();
        return uploadimage;
    }

    public static File writeImageToTkpdPath(byte[] buffer) throws IOException {
        File directory = new File(FileUtils.getFolderPathForUploadRandom());
        if (!directory.exists()) {
            directory.mkdirs();
        }
        File photo = new File(directory.getAbsolutePath() + "/image.jpg");

        if (photo.exists()) {
            photo.delete();
        }

        FileOutputStream fos = new FileOutputStream(photo.getPath());

        fos.write(buffer);
        fos.close();

        return photo;
    }

    public static byte[] compressImage(String path) throws IOException {
        Log.d(TAG, "lokasi yang mau diupload " + path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        BitmapFactory.Options checksize = new BitmapFactory.Options();
        checksize.inPreferredConfig = Bitmap.Config.ARGB_8888;
        checksize.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, checksize);
        options.inSampleSize = ImageHandler.calculateInSampleSize(checksize);
        Bitmap tempPic = BitmapFactory.decodeFile(path, options);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        Bitmap tempPicToUpload = null;

        tempPic = ImageHandler.RotatedBitmap(tempPic, path);

        if (tempPic.getWidth() > 2048 || tempPic.getHeight() > 2048) {
            tempPicToUpload = ImageHandler.ResizeBitmap(tempPic, 2048);
        }
        else {
            tempPicToUpload = tempPic;
        }
        tempPicToUpload.compress(Bitmap.CompressFormat.JPEG, 70, bao);
        return bao.toByteArray();
    }



}
