package com.tokopedia.core.loyaltysystem.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.R;

/**
 * Created by ricoharisin on 9/21/15.
 * modified by m.normansyah on 3/1/16, using Glide
 */
public class LuckyShopImage {

    public static void loadImage(final ImageView image, String url) {
        if (url != null && !url.equals("")) {
            ImageHandler.loadImageBitmap2(image.getContext(), url,
                    new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (bitmap.getHeight() <= 1 && bitmap.getWidth() <= 1) {
                                image.setVisibility(View.GONE);
                            } else {
                                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
                                image.setVisibility(View.VISIBLE);
                                image.setImageBitmap(scaled);
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
            ImageHandler.loadImageBitmap2(view.getContext(), url,
                    new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
                            ImageView image = (ImageView) view.findViewById(R.id.badge);
                            if (bitmap.getHeight() <= 1 && bitmap.getWidth() <= 1) {
                                view.setVisibility(View.GONE);
                            } else {
                                Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 30, 30, true);
                                image.setImageBitmap(scaled);
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
