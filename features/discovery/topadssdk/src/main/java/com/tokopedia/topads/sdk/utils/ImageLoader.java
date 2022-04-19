package com.tokopedia.topads.sdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.domain.model.Badge;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;

import java.util.List;

/**
 * @author by errysuprayogi on 4/3/17.
 */

public class ImageLoader {

    private static final String IMAGE_CACHE_DIR = "images";

    private Context context;
    private final String PATH_VIEW = "views";
    private static final String className = "com.tokopedia.topads.sdk.utils.ImageLoader";

    public ImageLoader(Context context) {
        this.context = context;
    }

    public void loadImage(String ecs, ImageView imageView) {
        loadImage(ecs, null, imageView);
    }

    public void loadImage(Product product, final ImageView imageView, int pos) {
        loadImage(product, imageView, pos,null);
    }

    public void loadImage(Product product, final ImageView imageView, int pos,
                          TopAdsItemImpressionListener impressionListener) {
        Glide.with(context)
                .asBitmap()
                .load(product.getImage().getS_ecs())
                .placeholder(R.drawable.loading_page)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                        if (!product.isLoaded()) {
                            product.setLoaded(true);
                            new ImpresionTask(className).execute(product.getImage().getS_url());
                            if(impressionListener!=null){
                                impressionListener.onImpressionProductAdsItem(pos, product, null);
                            }
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void loadImage(Shop shop, final ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .load(shop.getImageShop().getXsEcs())
                .placeholder(R.drawable.loading_page)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                        if (!shop.isLoaded()) {
                            shop.setLoaded(true);
                            new ImpresionTask(className).execute(shop.getImageShop().getsUrl());
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void loadImage(String ecs, final String url, final ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .load(ecs)
                .placeholder(R.drawable.loading_page)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        imageView.setImageBitmap(resource);
                        if (url!=null && url.contains(PATH_VIEW)) {
                            new ImpresionTask(className).execute(url);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    public void loadCircle(Shop shop, final ImageView imageView) {
        Glide.with(context)
                .asBitmap()
                .load(shop.getImageShop().getXsEcs())
                .placeholder(R.drawable.loading_page)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable circularBitmapDrawable =
                                RoundedBitmapDrawableFactory.create(imageView.getContext()
                                        .getResources(), resource);
                        circularBitmapDrawable.setCircular(true);
                        imageView.setImageDrawable(circularBitmapDrawable);

                        if (!shop.isLoaded()) {
                            shop.setLoaded(true);
                            new ImpresionTask(className).execute(shop.getImageShop().getsUrl());
                        }
                    }
                });
    }

    public void loadBadge(final LinearLayout container, List<Badge> badges) {
        container.removeAllViews();
        for (Badge badge : badges) {
            if(badge.isShow()) {
                final View view = LayoutInflater.from(context).inflate(R.layout.layout_badge, null);
                final ImageView imageView = (ImageView) view.findViewById(R.id.badge);
                Glide.with(context).load(badge.getImageUrl()).listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        container.addView(view);
                        return true;
                    }
                }).into(imageView);
            }
        }
    }

    public static void clearImage(final ImageView imageView) {
        if (imageView != null) {
            Glide.with(imageView.getContext()).clear(imageView);
            imageView.setImageDrawable(
                    getDrawable(imageView.getContext(), R.drawable.ic_loading_image)
            );
        }
    }

    public static Drawable getDrawable(Context context, int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            return context.getResources().getDrawable(
                    resId,
                    context.getApplicationContext().getTheme()
            );
        } else {
            return AppCompatResources.getDrawable(context, resId);
        }
    }

}
