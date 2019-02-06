package com.tokopedia.core.loyaltysystem.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.core2.R;

/**
 * Created by ricoharisin on 9/21/15.
 * modified by m.normansyah on 3/1/16, using Glide
 */
public class LuckyShopImage {

    public static void loadImage(final ImageView image, String url) {
        if (url != null && !url.equals("")) {
            Glide.with(image.getContext())
                    .load(url)
                    .asBitmap()
                    .dontAnimate()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (bitmap.getHeight() <= 1 && bitmap.getWidth() <= 1) {
                                image.setVisibility(View.GONE);
                            } else {
                                image.setVisibility(View.VISIBLE);
                                image.setImageBitmap(bitmap);
                            }
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            image.setVisibility(View.GONE);
                        }
                    });
        }
    }

    public static void loadImage(Context context, String url, final LinearLayout container) {
        if (url != null && !url.equals("")) {
            final View view = LayoutInflater.from(context).inflate(R.layout.badge_layout, null);
            Glide.with(context)
                    .load(url)
                    .asBitmap()
                    .dontAnimate()
                    .error(R.drawable.error_drawable)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            ImageView image = (ImageView) view.findViewById(R.id.badge);
                            if (bitmap.getHeight() <= 1 && bitmap.getWidth() <= 1) {
                                view.setVisibility(View.GONE);
                            } else {
                                image.setImageBitmap(bitmap);
                                view.setVisibility(View.VISIBLE);
                                container.addView(view);
                            }
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            view.setVisibility(View.GONE);
                        }
                    });
        }
    }
}
