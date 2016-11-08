package com.tokopedia.core.loyaltysystem.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;

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

    public static void loadImage(final ImageView image, String url, final int stateView) {
        //url = "https://clover-staging.tokopedia.com/badges/merchant/v1?shop_id=318446";
        if (!url.equals("")) {
            ImageHandler.loadImageBitmap2(image.getContext(), url, new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap>
                        glideAnimation) {
                    CommonUtils.dumper("Lucky bitmap: " + bitmap.getHeight());
                    if (bitmap.getHeight() <= 1 && bitmap.getWidth() <= 1) {
                        image.setVisibility(stateView);
                    } else {
                        //Bitmap newbitmap = ImageHandler.ResizeBitmap(bitmap, 0.5f);
                        image.setVisibility(View.VISIBLE);
                        image.setImageBitmap(bitmap);
                    }
                }
            });

//            ImageHandler.LoadImageBitmap(url, new Target() {
//                @Override
//                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {
//                    CommonUtils.dumper("Lucky bitmap: " + bitmap.getHeight());
//                    if (bitmap.getHeight() <= 1 && bitmap.getWidth() <= 1) {
//                        image.setVisibility(stateView);
//                    } else {
//                        //Bitmap newbitmap = ImageHandler.ResizeBitmap(bitmap, 0.5f);
//                        image.setVisibility(View.VISIBLE);
//                        image.setImageBitmap(bitmap);
//                    }
//
//                }
//
//                @Override
//                public void onBitmapFailed(Drawable drawable) {
//
//                }
//
//                @Override
//                public void onPrepareLoad(Drawable drawable) {
//
//                }
//            });
        }
    }
}
