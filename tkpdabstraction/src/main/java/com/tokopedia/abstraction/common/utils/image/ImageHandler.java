package com.tokopedia.abstraction.common.utils.image;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;
import android.util.Base64;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.bumptech.glide.signature.ObjectKey;
import com.tokopedia.abstraction.R;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;

import timber.log.Timber;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;


public class ImageHandler {

    public static final int IMAGE_WIDTH_HD = 1280;
    public static final int IMAGE_WIDTH_MIN = 480;

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
                    .placeholder(R.drawable.loading_page)
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

    public static void loadImageWithoutPlaceholder(ImageView imageview, String url, Drawable drawable) {
        if (url != null && !TextUtils.isEmpty(url)) {
            Glide.with(imageview.getContext())
                    .load(url)
                    .dontAnimate()
                    .error(drawable)
                    .into(imageview);
        } else {
            Glide.with(imageview.getContext())
                    .load(url)
                    .placeholder(drawable)
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

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
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

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void loadImageWithoutFit(Context context, ImageView imageview, String url) {
        Glide.with(context)
                .load(url)
                .placeholder(R.drawable.loading_page)
                .error(R.drawable.error_drawable)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageview);
    }

    public static void loadImageThumbs(Context context, ImageView imageview, String url) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(R.drawable.loading_page)
                .error(R.drawable.error_drawable)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageview);
    }

    public static void loadImage(Context context, ImageView imageview, String url, ColorDrawable colorDrawable) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(colorDrawable)
                .error(colorDrawable)
                .transition(withCrossFade())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageview);
    }


    /**
     * this class is not good for performances. please use LoadImageCustom
     *
     * @param imageview
     * @param url
     */
    public static void LoadImage(ImageView imageview, String url) {

        if (imageview.getContext() != null) {
            Glide.with(imageview.getContext())
                    .load(url)
                    .fitCenter()
                    .dontAnimate()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .into(imageview);
        }
    }

    public static void loadImageWithoutPlaceholder(ImageView imageview, String url) {

        if (imageview.getContext() != null) {
            Glide.with(imageview.getContext())
                    .load(url)
                    .centerCrop()
                    .dontAnimate()
                    .error(R.drawable.error_drawable)
                    .into(imageview);
        }
    }

    public static void loadImageWithoutPlaceholder(ImageView imageview, String url, ImageLoaderStateListener imageLoaderStateListener) {
        if (imageview.getContext() != null) {
            Glide.with(imageview.getContext())
                    .load(url)
                    .centerCrop()
                    .dontAnimate()
                    .error(R.drawable.error_drawable)
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@androidx.annotation.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            imageLoaderStateListener.failedLoad();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            imageLoaderStateListener.successLoad();
                            return false;
                        }
                    })
                    .into(imageview);
        }
    }

    public static void loadImageWithoutPlaceholderAndError(ImageView imageview, String url) {

        if (imageview.getContext() != null) {
            Glide.with(imageview.getContext())
                    .load(url)
                    .dontAnimate()
                    .into(imageview);
        }
    }

    public static void loadImageWithTarget(Context context, String url, CustomTarget<Bitmap> simpleTarget) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .fitCenter()
                .dontAnimate()
                .placeholder(R.drawable.loading_page)
                .error(R.drawable.error_drawable)
                .into(simpleTarget);
    }

    public static void loadImageWithTargetCenterCrop(Context context, String url, CustomTarget<Bitmap> simpleTarget) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .fitCenter()
                .centerCrop()
                .dontAnimate()
                .placeholder(R.drawable.loading_page)
                .error(R.drawable.error_drawable)
                .into(simpleTarget);
    }

    public static void loadImage2(ImageView imageview, String url, int resId) {
        Drawable error = AppCompatResources.getDrawable(imageview.getContext(), resId);
        if (url != null && !TextUtils.isEmpty(url)) {
            Glide.with(imageview.getContext())
                    .load(url)
                    .placeholder(R.drawable.loading_page)
                    .dontAnimate()
                    .error(error)
                    .into(imageview);
        } else {
            Glide.with(imageview.getContext())
                    .load(url)
                    .placeholder(resId)
                    .error(error)
                    .into(imageview);
        }
    }

    public static void loadImageAndCache(ImageView imageview, String url) {
        Glide.with(imageview.getContext())
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .dontAnimate()
                .into(imageview);
    }

    public static void loadImageWithSignature(ImageView imageview, String url, ObjectKey signature) {
        try {
            Glide.with(imageview.getContext())
                    .load(url)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .dontAnimate()
                    .signature(signature)
                    .into(imageview);
        } catch (IllegalArgumentException e){
            Timber.e("%s%s", url, e.getMessage());
        }
    }

    public static void downloadOriginalSizeImageWithSignature(
            Context context,
            String url,
            ObjectKey stringSignature,
            RequestListener<Drawable> backgroundImgRequestListener
    ) {
        Glide.with(context)
                .load(url)
                .signature(stringSignature)
                .listener(backgroundImgRequestListener)
                .submit(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
    }

    public static void loadImageCover2(ImageView imageview, String url) {
        Glide.with(imageview.getContext())
                .load(url)
                .into(imageview);
    }


    public static void loadImageBitmap2(Context context, String url, CustomTarget<Bitmap> target) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .dontAnimate()
                .placeholder(R.drawable.loading_page)
                .error(R.drawable.error_drawable)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .centerCrop()
                .into(target);
    }

    public static void loadImageCircle2(Context context, final ImageView imageView,
                                        final String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .dontAnimate()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .into(getCircleImageViewTarget(imageView));
        }
    }

    public static void loadImageCircle2(Context context, final ImageView imageView,
                                        final String url, int resIdEmpty) {
        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .dontAnimate()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .into(getCircleImageViewTarget(imageView));
        } else {
            Glide.with(context)
                    .asBitmap()
                    .load(resIdEmpty)
                    .dontAnimate()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .into(getCircleImageViewTarget(imageView));
        }
    }

    public static void loadImageCircle2(Context context, ImageView imageView, File file) {
        if (file != null && file.exists()) {
            Glide.with(context)
                    .asBitmap()
                    .load(file)
                    .dontAnimate()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .into(getCircleImageViewTarget(imageView));
        }
    }

    public static void loadImageRounded2(Context context, final ImageView imageview, final String url) {
        loadImageRounded2(context, imageview, url, 5.0f);
    }

    public static void loadImageRounded2(Context context, final ImageView imageview, final int resourceDrawable, float radius) {
        Glide.with(context)
                .asBitmap()
                .load(resourceDrawable)
                .dontAnimate()
                .placeholder(R.drawable.loading_page)
                .error(R.drawable.error_drawable)
                .into(getRoundedImageViewTarget(imageview, radius));
    }

    public static void loadImageRounded2(Context context, final ImageView imageview, final String url, float radius) {
        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .dontAnimate()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .into(getRoundedImageViewTarget(imageview, radius));
        }
    }

    public static void loadImageRounded(Context context, final ImageView imageview, final String url, float radius) {
        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .dontAnimate()
                    .centerCrop()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .into(getRoundedImageViewTarget(imageview, radius));
        }
    }

    public static void loadImageFit2(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(R.drawable.loading_page)
                .error(R.drawable.error_drawable)
                .centerCrop()
                .into(imageView);
    }

    public static void loadImageFitCenter(Context context, ImageView imageView, String url) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(R.drawable.loading_page)
                .error(R.drawable.error_drawable)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .fitCenter()
                .into(imageView);
    }

    private static BitmapImageViewTarget getCircleImageViewTarget(final ImageView imageView) {
        return new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        };
    }

    public static String encodeToBase64(String imagePath) {
        return encodeToBase64(imagePath, 100);
    }

    public static String encodeToBase64(String imagePath, int quality) {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public static String encodeToBase64(String imagePath, Bitmap.CompressFormat compressFormat) {
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(compressFormat, 100, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private static BitmapImageViewTarget getRoundedImageViewTarget(final ImageView imageView, final float radius) {
        return new BitmapImageViewTarget(imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(imageView.getContext().getResources(), resource);
                circularBitmapDrawable.setCornerRadius(radius);
                imageView.setImageDrawable(circularBitmapDrawable);
            }
        };
    }


    public static void loadImageFromFile(Context context, ImageView imageView, File file) {

        Glide.with(context)
                .load(file)
                .centerCrop()
                .into(imageView);
    }

    public static void loadImageFromFileFitCenter(Context context, ImageView imageView, File file) {

        Glide.with(context)
                .load(file)
                .centerCrop()
                .into(imageView);
    }


    public static void loadImageFromUriFitCenter(Context context, ImageView imageView, Uri uri) {

        Glide.with(context)
                .asBitmap() // some .jpeg files are actually gif
                .load(uri)
                .centerCrop()
                .into(imageView);
    }


    public static void LoadImageResize(Context context, ImageView imageView, String url, int width, int height) {
        Glide.with(context)
                .load(url)
                .override(width, height)
                .fitCenter()
                .into(imageView);
    }

    public static void loadGif(ImageView imageView, int gifDrawable, int placeholder) {
        if(placeholder < 0) {
            Glide.with(imageView.getContext())
                    .asGif()
                    .load(gifDrawable)
                    .into(imageView);
            return;
        }
        Drawable drawable = AppCompatResources.getDrawable(imageView.getContext(), placeholder);
        Glide.with(imageView.getContext())
                .asGif()
                .load(gifDrawable)
                .placeholder(drawable)
                .into(imageView);
    }

    public static void loadGifFromUrl(ImageView imageView, String url, int placeholder) {
        if(placeholder < 0) {
            loadImageWithoutPlaceholder(imageView, url);
            return;
        }
        Drawable drawable = AppCompatResources.getDrawable(imageView.getContext(), placeholder);
        Glide.with(imageView.getContext())
                .asGif()
                .load(url)
                .placeholder(drawable)
                .into(imageView);
    }

    public static void loadImage(Context context, ImageView imageview, String url, int placeholder) {
        if(placeholder < 0) {
            loadImageWithoutPlaceholder(imageview, url);
            return;
        }
        Drawable drawable = AppCompatResources.getDrawable(imageview.getContext(), placeholder);
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(drawable)
                .error(R.drawable.error_drawable)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageview);
    }

    public static void loadImage(Context context, ImageView imageview, String url, int placeholder, int error_image) {
        if(placeholder < 0) {
            loadImageWithoutPlaceholder(imageview, url);
            return;
        }
        Drawable drawable = AppCompatResources.getDrawable(imageview.getContext(), placeholder);
        Drawable errorDrawable = AppCompatResources.getDrawable(imageview.getContext(), error_image);
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(drawable)
                .error(errorDrawable)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .into(imageview);
    }

    public static void loadImageBlur(final Context context, final ImageView imageView, String imageUrl) {
        if (context != null && Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            Glide.with(context)
                    .load(imageUrl)
                    .override(80, 80)
                    .centerCrop()
                    .into(imageView);
        }

        if (context != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .thumbnail(Glide.with(context).asBitmap().load(imageUrl))
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @androidx.annotation.Nullable Transition<? super Bitmap> transition) {
                            Bitmap blurredBitmap = blur(context, resource);
                            imageView.setImageBitmap(blurredBitmap);
                        }

                        @Override
                        public void onLoadCleared(@androidx.annotation.Nullable Drawable placeholder) {

                        }
                    });
        }
    }

    public static void loadImageBlurWithViewTarget(final Context context,
                                                   String imageUrl,
                                                   CustomTarget<Bitmap> simpleTarget) {
        if (context != null && Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .centerCrop()
                    .into(simpleTarget);
        } else if (context != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Glide.with(context)
                    .asBitmap()
                    .load(imageUrl)
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .transform(new CenterCrop(), blurTransformation(context))
                    .into(simpleTarget);
        }
    }

    private static BitmapTransformation blurTransformation(Context context) {
        return new BitmapTransformation() {
            @Override
            public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
                messageDigest.update(getClass().getName().getBytes(Charset.forName("UTF-8")));
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return blurStrong(context, toTransform);
            }
        };
    }

    private static RenderScript rs;
    private static ScriptIntrinsicBlur theIntrinsic;
    private static RenderScript getRs(Context context){
        if (rs == null) {
            synchronized (RenderScript.class) {
                if (rs == null) {
                    rs = RenderScript.create(context);
                }
            }
        }
        return rs;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static ScriptIntrinsicBlur getIntrinsic(Context context){
        if (theIntrinsic == null) {
            synchronized (ScriptIntrinsicBlur.class) {
                if (theIntrinsic == null) {
                    theIntrinsic = ScriptIntrinsicBlur.create(getRs(context),
                            Element.U8_4(getRs(context)));
                }
            }
        }
        return theIntrinsic;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurStrong(Context context, Bitmap image) {
        final float BITMAP_SCALE = 0.04f;
        final float BLUR_RADIUS = 7.5f;

        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        Allocation tmpIn = Allocation.createFromBitmap(getRs(context), inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(getRs(context), outputBitmap);
        getIntrinsic(context).setRadius(BLUR_RADIUS);
        getIntrinsic(context).setInput(tmpIn);
        getIntrinsic(context).forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blur(Context context, Bitmap image) {
        final float BITMAP_SCALE = 0.4f;
        final float BLUR_RADIUS = 7.5f;

        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }

    public static void loadImageWithListener(
            ImageView imageview,
            String url,
            RequestListener<Drawable> requestListener
    ) {
        if (url != null) {
            Glide.with(imageview.getContext())
                    .load(url)
                    .dontAnimate()
                    .listener(requestListener)
                    .fitCenter()
                    .placeholder(R.drawable.loading_page)
                    .into(imageview);
        }
    }

    public static void clearImage(ImageView imageView) {
        try {
            if (imageView != null) {
                Glide.with(imageView.getContext()).clear(imageView);
            }
        } catch (IllegalArgumentException e){
            Timber.e("%s", e.getMessage());
        }
    }

    public static void loadImageBlurredWithListener(
            ImageView imageView,
            String url,
            int blurWidth,
            int blurHeight,
            RequestListener<Drawable> listener) {
        if (url != null) {
            Glide.with(imageView.getContext())
                    .load(url)
                    .dontAnimate()
                    .override(blurWidth, blurHeight)
                    .listener(listener)
                    .fitCenter()
                    .placeholder(R.drawable.loading_page)
                    .into(imageView);
        }
    }

    public static void loadBackgroundImage(@Nullable Window window, @NotNull String backgroundUrl) {
        if (window != null && window.getContext() != null) {
            Glide.with(window.getContext())
                    .load(backgroundUrl)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @androidx.annotation.Nullable Transition<? super Drawable> transition) {
                            window.setBackgroundDrawable(resource);
                        }

                        @Override
                        public void onLoadCleared(@androidx.annotation.Nullable Drawable placeholder) {

                        }
                    });
        }
    }

    public static void loadBackgroundImage(View view, String url) {
        if (view == null || !URLUtil.isValidUrl(url)) {
            return;
        }

        Glide.with(view.getContext())
                .asBitmap()
                .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @androidx.annotation.Nullable Transition<? super Bitmap> transition) {
                        if (resource.getWidth() > 1) {
                            view.setBackground(new BitmapDrawable(view.getResources(), resource));
                        }
                    }

                    @Override
                    public void onLoadCleared(@androidx.annotation.Nullable Drawable placeholder) {

                    }
                });
    }


    public static void cacheFromUrl(@NotNull Context context, @NotNull String url, @NotNull ArrayList<Drawable> cacheImageList) {
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 21, Resources.getSystem().getDisplayMetrics());
        Glide.with(context)
                .load(url)
                .override(size, size)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @androidx.annotation.Nullable Transition<? super Drawable> transition) {
                        cacheImageList.add(resource);
                    }

                    @Override
                    public void onLoadCleared(@androidx.annotation.Nullable Drawable placeholder) {

                    }
                });
    }
    public interface ImageLoaderStateListener{
        void successLoad();
        void failedLoad();
    }
}

