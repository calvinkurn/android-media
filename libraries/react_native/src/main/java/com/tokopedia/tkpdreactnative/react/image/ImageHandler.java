package com.tokopedia.tkpdreactnative.react.image;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.IOException;

public class ImageHandler extends com.tokopedia.abstraction.common.utils.image.ImageHandler {

    public static Bitmap ResizeBitmap(Bitmap bitmap, float bounding) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float xScale = bounding / width;
        float yScale = bounding / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    /**
     * rotate bitmap if only jpeg, not for other extension
     *
     * @param bitmap
     * @param file
     * @return
     * @throws IOException
     */
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


    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
}
