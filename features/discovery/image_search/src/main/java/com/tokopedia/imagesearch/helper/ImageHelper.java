package com.tokopedia.imagesearch.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import com.bumptech.glide.Glide;

import java.io.IOException;

import static com.tokopedia.abstraction.common.utils.image.ImageHandler.flip;

public class ImageHelper {

    public static Bitmap resizeImage(Bitmap image, int maxWidth, int maxHeight) {
        int width = image.getWidth();
        int height = image.getHeight();
        float ratioBitmap = (float) width / (float) height;
        float ratioMax = (float) maxWidth / (float) maxHeight;

        int finalWidth = maxWidth;
        int finalHeight = maxHeight;
        if (ratioMax > ratioBitmap) {
            finalWidth = (int) ((float) maxHeight * ratioBitmap);
        } else {
            finalHeight = (int) ((float) maxWidth / ratioBitmap);
        }
        image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
        return image;
    }

    public static Bitmap getBitmapFromUri(Context context, Uri uri, int width, int height) {
        Bitmap bitmap = null;
        try {
            bitmap = Glide.with(context).asBitmap().load(uri).into(width, height).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap RotatedBitmap(Bitmap bitmap, String file) throws IOException {
        ExifInterface exif = new ExifInterface(file);
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        if (orientation == ExifInterface.ORIENTATION_FLIP_HORIZONTAL)
            return flip(bitmap, true, false);
        if (orientation == ExifInterface.ORIENTATION_FLIP_VERTICAL)
            return flip(bitmap, false, true);
        if (rotationAngle == 0) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
