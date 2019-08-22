package com.tkpd.library.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import androidx.appcompat.content.res.AppCompatResources;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
//import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.abstraction.common.utils.view.CommonUtils;
import com.tokopedia.core.network.R;
import com.tokopedia.core.gcm.BuildAndShowNotification;

import java.io.File;
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

    private static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

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
//            bitmap = Glide.with(context).load(uri).asBitmap().into(width, height).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static void loadImageWithId(ImageView imageview, int resId) {
        if (imageview.getContext() != null) {
            Drawable drawable = AppCompatResources.getDrawable(imageview.getContext(), resId);
            Glide.with(imageview.getContext())
                    .load("")
                    .placeholder(R.drawable.loading_page)
                    .dontAnimate()
                    .error(drawable)
                    .into(imageview);
        }
    }

    public static void loadImageWithId(ImageView imageview, int resId, int placeholder) {
        if (imageview.getContext() != null) {
            Drawable drawable = AppCompatResources.getDrawable(imageview.getContext(), resId);
            Glide.with(imageview.getContext())
                    .load("")
                    .placeholder(placeholder)
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


    public static void loadImageWithoutPlaceholder(ImageView imageview, String url, int resId) {
        if (url != null && !TextUtils.isEmpty(url)) {
            Glide.with(imageview.getContext())
                    .load(url)
                    .dontAnimate()
                    .error(resId)
                    .into(imageview);
        } else {
            Glide.with(imageview.getContext())
                    .load(url)
                    .placeholder(resId)
                    .error(resId)
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

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        // Fill in bottom corners

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void loadImageWithoutFit(Context context, ImageView imageview, String url) {
        if (isContextValid(context)) {
//            Glide.with(context)
//                    .load(url)
//                    .placeholder(R.drawable.loading_page)
//                    .error(R.drawable.error_drawable)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into(imageview);
        }
    }

    public static void loadImageThumbs(Context context, ImageView imageview, String url) {
        if (isContextValid(context)) {
//            Glide.with(context)
//                    .load(url)
//                    .dontAnimate()
//                    .placeholder(R.drawable.loading_page)
//                    .error(R.drawable.error_drawable)
//                    .skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                    .into(imageview);
        }
    }

    /* This method specifically designed to display cached image from different size when offline */
    public static void loadImageSourceSize(Context context, ImageView imageview, String url) {
        if (isContextValid(context)) {
//            Glide.with(context)
//                    .load(url)
//                    .dontAnimate()
//                    .placeholder(R.drawable.loading_page)
//                    .error(R.drawable.error_drawable)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into(imageview);
        }
    }

    /* This method specifically designed to display cached image from different size when offline */
    /* For product detail page only */
    public static void loadImageSourceSizeFitCenter(Context context, ImageView imageview, String url) {
        if (isContextValid(context)) {
//            Glide.with(context.getApplicationContext())
//                    .load(url)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .fitCenter()
//                    .into(imageview);
        }
    }

    /* This method specifically designed to display cached image from different size when offline */
    /* For product detail page only */
    public static void loadImageSourceSizeCenterCrop(Context context, ImageView imageview, String url) {
        if (isContextValid(context)) {
//            Glide.with(context.getApplicationContext())
//                    .load(url)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .centerCrop()
//                    .into(imageview);
        }
    }

    public static void loadImage(Context context, ImageView imageview, String url, int placeholder) {
        if (isContextValid(context)) {
//            Glide.with(context)
//                    .load(url)
//                    .dontAnimate()
//                    .placeholder(placeholder)
//                    .error(R.drawable.error_drawable)
//                    .skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into(imageview);
        }
    }

    public static void loadImage(Context context, ImageView imageview, String url, int placeholder, int error_image) {
        if (isContextValid(context)) {
//            Glide.with(context)
//                    .load(url)
//                    .dontAnimate()
//                    .placeholder(placeholder)
//                    .error(error_image)
//                    .skipMemoryCache(true)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .into(imageview);
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
                        .placeholder(R.drawable.loading_page)
                        .error(R.drawable.error_drawable)
                        .into(imageview);
            } catch (Exception e) {
            }
        }
    }

    public static void loadImageCenterCrop(ImageView imageview, String url) {
        if (imageview.getContext() != null) {
            try {
                Glide.with(imageview.getContext())
                        .load(url)
                        .centerCrop()
                        .dontAnimate()
                        .placeholder(R.drawable.loading_page)
                        .error(R.drawable.error_drawable)
                        .into(imageview);
            } catch (Exception e) {
            }
        }
    }

    public static void loadImageWithTarget(Context context, String url, SimpleTarget<Bitmap> simpleTarget) {
        if (isContextValid(context)) {
//            Glide.with(context)
//                    .load(url)
//                    .asBitmap()
//                    .fitCenter()
//                    .dontAnimate()
//                    .placeholder(R.drawable.loading_page)
//                    .error(R.drawable.error_drawable)
//                    .into(simpleTarget);
        }
    }

    public static void loadImage2(ImageView imageview, String url, int resId) {
        if (url != null && !TextUtils.isEmpty(url)) {
            Glide.with(imageview.getContext())
                    .load(url)
                    .placeholder(R.drawable.loading_page)
                    .dontAnimate()
                    .error(resId)
                    .into(imageview);
        } else {
            Glide.with(imageview.getContext())
                    .load(url)
                    .placeholder(resId)
                    .error(resId)
                    .into(imageview);
        }
    }

//    public static void loadImageChat(ImageView imageview, String url, RequestListener<String, GlideDrawable> requestListener) {
//        if (url != null) {
//            Glide.with(imageview.getContext())
//                    .load(url)
//                    .dontAnimate()
//                    .listener(requestListener)
//                    .fitCenter()
//                    .placeholder(R.drawable.loading_page)
//                    .into(imageview);
//        }
//    }

//    public static void loadImageChatBlurred(ImageView imageview, String url, RequestListener<String, GlideDrawable> requestListener) {
//        if (url != null) {
//            Glide.with(imageview.getContext())
//                    .load(url)
//                    .dontAnimate()
//                    .override(30, 30)
//                    .listener(requestListener)
//                    .fitCenter()
//                    .placeholder(R.drawable.loading_page)
//                    .into(imageview);
//        }
//    }

    public static void loadImageAndCache(ImageView imageview, String url) {
//        Glide.with(imageview.getContext())
//                .load(url)
//                .diskCacheStrategy(DiskCacheStrategy.RESULT)
//                .dontAnimate()
//                .into(imageview);
    }

    public static void loadImageCover2(ImageView imageview, String url) {
        Glide.with(imageview.getContext())
                .load(url)
                .into(imageview);
    }


    public static void loadImageBitmap2(Context context, String url, SimpleTarget<Bitmap> target) {
        if (isContextValid(context)) {
//            Glide.with(context)
//                    .load(url)
//                    .asBitmap()
//                    .dontAnimate()
//                    .placeholder(R.drawable.loading_page)
//                    .error(R.drawable.error_drawable)
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .centerCrop()
//                    .into(target);
        }
    }

    public static void loadImageBitmapNotification(Context context, String url, BuildAndShowNotification.
            OnGetFileListener listener) {
        FutureTarget<File> futureTarget = Glide.with(context)
                .load(url)
                .downloadOnly(210, 100);

        try {
            File file = futureTarget.get();
            if (file.exists()) {
                listener.onFileReady(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void LoadImageWGender(ImageView imageview, String url, Context context, String
            gender) {
        if (!url.equals("null")) {
            loadImageCircle2(imageview.getContext(), imageview, url);
        } else {
            if (gender.equals("1")) {
                imageview.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_image_avatar_boy), 100));
            } else {
                imageview.setImageBitmap(getRoundedCornerBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_image_avatar_girl), 100));
            }
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
