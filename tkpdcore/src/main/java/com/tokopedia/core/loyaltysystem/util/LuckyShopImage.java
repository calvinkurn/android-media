package com.tokopedia.core.loyaltysystem.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.tokopedia.core2.R;

/**
 * Created by ricoharisin on 9/21/15.
 * modified by m.normansyah on 3/1/16, using Glide
 */
public class LuckyShopImage {

    public static void loadImage(final ImageView image, String url) {
        if (url != null && !url.equals("")) {
            Glide.with(image.getContext())
                    .asBitmap()
                    .load(url)
                    .dontAnimate()
                    .placeholder(R.drawable.loading_page)
                    .error(R.drawable.error_drawable)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            if (resource.getHeight() <= 1 && resource.getWidth() <= 1) {
                                image.setVisibility(View.GONE);
                            } else {
                                image.setVisibility(View.VISIBLE);
                                image.setImageBitmap(resource);
                            }
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            image.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
    }

    public static void loadImage(Context context, String url, final LinearLayout container) {
        if (url != null && !url.equals("")) {
            final View view = LayoutInflater.from(context).inflate(R.layout.badge_layout, null);
            Glide.with(context)
                    .asBitmap()
                    .load(url)
                    .dontAnimate()
                    .error(R.drawable.error_drawable)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            ImageView image = (ImageView) view.findViewById(R.id.badge);
                            if (resource.getHeight() <= 1 && resource.getWidth() <= 1) {
                                view.setVisibility(View.GONE);
                            } else {
                                image.setImageBitmap(resource);
                                view.setVisibility(View.VISIBLE);
                                container.addView(view);
                            }
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            view.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {

                        }
                    });
        }
    }
}
