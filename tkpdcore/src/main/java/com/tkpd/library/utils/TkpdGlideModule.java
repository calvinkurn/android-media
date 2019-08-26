package com.tkpd.library.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalPreferredCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by m.normansyah on 3/1/16.
 */
public class TkpdGlideModule extends AppGlideModule {

    public static final String TOKOPEDIA = "tokopedia";

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        long cacheSize = 512*1024*1024;
        RequestOptions options = new RequestOptions().format(DecodeFormat.PREFER_ARGB_8888);
        builder.setDiskCache(new ExternalPreferredCacheDiskCacheFactory(context, TOKOPEDIA, cacheSize));
        builder.setMemoryCache(new LruResourceCache(8 * 1024 * 1024));
        builder.setBitmapPool(new LruBitmapPool(8 * 1024 * 1024));
        builder.setDefaultRequestOptions(options);
    }
}
