package com.tokopedia.abstraction.common.utils.image;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by m.normansyah on 3/1/16.
 */
public class TkpdGlideModule implements GlideModule {

    public static final String TOKOPEDIA = "tokopedia";

    public TkpdGlideModule() {
        super();
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        long cacheSize = 512*1024*1024;
        RequestOptions options = new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888);
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, TOKOPEDIA, cacheSize));
        builder.setMemoryCache(new LruResourceCache(8 * 1024 * 1024));
        builder.setBitmapPool(new LruBitmapPool(8 * 1024 * 1024));
        builder.setDefaultRequestOptions(options);
    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }
}
