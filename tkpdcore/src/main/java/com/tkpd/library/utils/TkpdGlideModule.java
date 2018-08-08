package com.tkpd.library.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by m.normansyah on 3/1/16.
 */
public class TkpdGlideModule implements GlideModule{

    public static final String TOKOPEDIA = "tokopedia";

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        builder.setDiskCache(
                new ExternalCacheDiskCacheFactory(context, TOKOPEDIA, 512*1024*1024));
        builder.setMemoryCache(new LruResourceCache(8 * 1024 * 1024));
        builder.setBitmapPool(new LruBitmapPool(8 * 1024 * 1024));
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
