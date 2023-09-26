package com.tkpd.library.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.widget.ImageView;

import androidx.appcompat.content.res.AppCompatResources;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.core.network.R;

import java.io.IOException;

@Deprecated
public class ImageHandler extends com.tokopedia.abstraction.common.utils.image.ImageHandler {

    public static Bitmap ResizeBitmap(Bitmap bitmap, float bounding) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float xScale = bounding / width;
        float yScale = bounding / height;
        float scale = (xScale <= yScale) ? xScale : yScale;
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);

        return scaledBitmap;
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

    public static void loadImageWithId(ImageView imageview, int resId) {
        if (imageview.getContext() != null) {
            Drawable drawable = AppCompatResources.getDrawable(imageview.getContext(), resId);
            Glide.with(imageview.getContext())
                    .load("")
                    .placeholder(com.tokopedia.abstraction.R.drawable.loading_page)
                    .dontAnimate()
                    .error(drawable)
                    .into(imageview);
        }
    }

    public static void loadImageWithIdWithoutPlaceholder(ImageView imageview, int resId) {
        if (imageview.getContext() != null) {
            Drawable drawable = AppCompatResources.getDrawable(imageview.getContext(), resId);
            Glide.with(imageview.getContext())
                    .load("")
                    .placeholder(drawable)
                    .dontAnimate()
                    .error(drawable)
                    .into(imageview);
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > IMAGE_WIDTH_HD || width > IMAGE_WIDTH_HD) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > IMAGE_WIDTH_MIN
                    && (halfWidth / inSampleSize) > IMAGE_WIDTH_MIN) {
                inSampleSize = inSampleSize * 2;
            }
        }
        return inSampleSize;
    }

    public static void loadImageThumbs(Context context, ImageView imageview, String url) {
        if (isContextValid(context)) {
            Glide.with(context)
                    .load(url)
                    .dontAnimate()
                    .placeholder(com.tokopedia.abstraction.R.drawable.loading_page)
                    .error(com.tokopedia.abstraction.R.drawable.error_drawable)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(imageview);
        }
    }

    public static void loadImage(Context context, ImageView imageview, String url, int placeholder) {
        if (isContextValid(context)) {
            Glide.with(context)
                    .load(url)
                    .dontAnimate()
                    .placeholder(placeholder)
                    .error(com.tokopedia.abstraction.R.drawable.error_drawable)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imageview);
        }
    }

    public static void loadImage(Context context, ImageView imageview, String url, int placeholder, int error_image) {
        if (isContextValid(context)) {
            Glide.with(context)
                    .load(url)
                    .dontAnimate()
                    .placeholder(placeholder)
                    .error(error_image)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(imageview);
        }
    }


    /**
     * this class is not good for performances. please use LoadImageCustom
     *
     * @param imageview
     * @param url
     */
    public static void LoadImage(ImageView imageview, String url) {
        if (imageview.getContext() != null) {
            try {
                Glide.with(imageview.getContext())
                        .load(url)
                        .fitCenter()
                        .dontAnimate()
                        .placeholder(com.tokopedia.abstraction.R.drawable.loading_page)
                        .error(com.tokopedia.abstraction.R.drawable.error_drawable)
                        .into(imageview);
            } catch (Exception e) {
            }
        }
    }

    public static void loadImageWithTarget(Context context, String url, CustomTarget<Bitmap> simpleTarget) {
        if (isContextValid(context)) {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .fitCenter()
                    .dontAnimate()
                    .placeholder(com.tokopedia.abstraction.R.drawable.loading_page)
                    .error(com.tokopedia.abstraction.R.drawable.error_drawable)
                    .into(simpleTarget);
        }
    }

    private static boolean isContextValid(Context context) {
        Context tempContext = context;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {
            tempContext = CommonUtils.getActivity(context);
        }
        return (tempContext instanceof Activity && !((Activity) tempContext).isFinishing()) || tempContext instanceof Application;
    }
}
