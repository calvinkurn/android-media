package com.tokopedia.topads.sdk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.content.res.AppCompatResources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.domain.model.Badge;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.imageutils.ImageCache;
import com.tokopedia.topads.sdk.imageutils.ImageFetcher;
import com.tokopedia.topads.sdk.imageutils.ImageWorker;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;

import java.util.List;

/**
 * @author by errysuprayogi on 4/3/17.
 */

public class ImageLoader {

    private static final String IMAGE_CACHE_DIR = "images";

    private Context context;
    private ImageCache.ImageCacheParams cacheParams;
    private ImageFetcher imageFetcher;
    private final String PATH_VIEW = "views";

    public ImageLoader(Context context) {
        this.context = context;
        cacheParams = new ImageCache.ImageCacheParams(context, IMAGE_CACHE_DIR);
        cacheParams.setMemCacheSizePercent(0.25f);
        imageFetcher = new ImageFetcher(context,
                context.getResources().getDimensionPixelSize(R.dimen.image_thumbnail_size));
        imageFetcher.setLoadingImage(R.drawable.loading_page);
        imageFetcher.addImageCache(cacheParams);
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
                .load(product.getImage().getS_ecs())
                .asBitmap()
                .placeholder(R.drawable.loading_page)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                        if (!product.isLoaded()) {
                            product.setLoaded(true);
                            new ImpresionTask().execute(product.getImage().getS_url());
                            if(impressionListener!=null){
                                impressionListener.onImpressionProductAdsItem(pos, product);
                            }
                        }
                    }
                });
    }

    public void loadImage(Shop shop, final ImageView imageView) {
        Glide.with(context)
                .load(shop.getImageShop().getXsEcs())
                .asBitmap()
                .placeholder(R.drawable.loading_page)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                        if (!shop.isLoaded()) {
                            shop.setLoaded(true);
                            new ImpresionTask().execute(shop.getImageShop().getsUrl());
                        }
                    }
                });
    }

    public void loadImage(String ecs, final String url, final ImageView imageView) {
        Glide.with(context)
                .load(ecs)
                .asBitmap()
                .placeholder(R.drawable.loading_page)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imageView.setImageBitmap(resource);
                        if (url!=null && url.contains(PATH_VIEW)) {
                            new ImpresionTask().execute(url);
                        }
                    }
                });
    }

    public void loadImageWithMemoryCache(String url, ImageView imageView){
        imageFetcher.loadImage(url, imageView);
    }

    public void loadCircle(Shop shop, final ImageView imageView) {
        Glide.with(context)
                .load(shop.getImageShop().getXsEcs())
                .asBitmap()
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
                            new ImpresionTask().execute(shop.getImageShop().getsUrl());
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
                imageFetcher.loadImage(badge.getImageUrl(), imageView, new ImageWorker.OnImageLoadedListener() {
                    @Override
                    public void onImageLoaded(boolean success) {
                        if (success) {
                            container.addView(view);
                        }
                    }
                }, true);
            }
        }
    }

    public static void clearImage(final ImageView imageView) {
        if (imageView != null) {
            Glide.clear(imageView);
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

    public static int getRatingDrawable(int param) {
        switch (param) {
            case 0:
                return R.drawable.ic_star_none;
            case 1:
                return R.drawable.icon_star_one;
            case 2:
                return R.drawable.icon_star_two;
            case 3:
                return R.drawable.icon_star_three;
            case 4:
                return R.drawable.icon_star_four;
            case 5:
                return R.drawable.icon_star_five;
            default:
                return R.drawable.ic_star_none;
        }
    }


}
