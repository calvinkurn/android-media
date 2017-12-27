package com.tkpd.library.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.tokopedia.core.gcm.BuildAndShowNotification;
import com.tokopedia.core.R;

import java.io.File;

@Deprecated
public class ImageHandler extends com.tokopedia.abstraction.utils.image.ImageHandler {

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

    public static void loadImageCircle2(Context context, final ImageView imageView,
                                        final String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(context)
                    .load(url)
                    .asBitmap()
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
                    .load(url)
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .into(getCircleImageViewTarget(imageView));
        } else {
            Glide.with(context)
                    .load(resIdEmpty)
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .into(getCircleImageViewTarget(imageView));
        }
    }

    public static void loadImageCircle2(Context context, ImageView imageView, File file) {
        if (file != null && file.exists()) {
            Glide.with(context)
                    .load(file)
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .into(getCircleImageViewTarget(imageView));
        }
    }

    public static void loadImageRounded2Target(final Context context,
                                               final ImageView imageview, final String url) {
        Glide.with(context)
                .load(url)
                .asBitmap()
                .dontAnimate()
                .placeholder(R.drawable.loading_page)
                .error(R.drawable.error_drawable)
                .into(new SimpleTarget<Bitmap>() {

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        imageview.setImageDrawable(placeholder);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        float newWidth = context.getResources().getDimension(R.dimen.half_screen) * 2;
                        int width = resource.getWidth();
                        int height = resource.getHeight();
                        float newHeight = (newWidth * height) / width;

                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageview.getLayoutParams();
                        params.width = (int) newWidth;
                        params.height = (int) newHeight;
                        imageview.setLayoutParams(params);

                        imageview.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        imageview.setImageDrawable(errorDrawable);
                    }
                });
    }

    public static void loadImageRounded2(Context context, final ImageView imageview, final String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(imageview.getContext())
                    .load(url)
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .into(getRoundedImageViewTarget(imageview, 5.0f));
        }
    }

    public static void loadImageRounded2(Fragment fragment, final ImageView imageview, final String url) {
        if (url != null && !url.isEmpty()) {
            Glide.with(fragment)
                    .load(url)
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .into(getRoundedImageViewTarget(imageview, 5.0f));
        }
    }

    public static void LoadImageWGender(ImageView imageview, String url, Activity context, String gender) {
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

    public static void loadImageFit2(Context context, ImageView imageView, File file) {
        Glide.with(context)
                .load(file)
                .dontAnimate()
                .placeholder(R.drawable.loading_page)
                .error(R.drawable.error_drawable)
                .centerCrop()
                .into(imageView);
    }

    public static void loadGif(ImageView imageView, int gifDrawable, int placeholder) {
        Glide.with(imageView.getContext()).load(gifDrawable)
                .asGif()
                .placeholder(placeholder)
                .into(imageView);
    }
}