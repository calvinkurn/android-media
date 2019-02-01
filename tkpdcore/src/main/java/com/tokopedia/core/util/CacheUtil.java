package com.tokopedia.core.util;

import android.content.Context;
import android.text.TextUtils;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.var.TkpdCache;

public class CacheUtil {

    public static void storeWebToAppPromoCodeIfExist(String promoCode, Context context) {
        if (!TextUtils.isEmpty(promoCode)) {
            LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.CACHE_PROMO_CODE);
            localCacheHandler.putString(TkpdCache.Key.KEY_CACHE_PROMO_CODE, promoCode);
            localCacheHandler.applyEditor();
        }
    }

    public static void removeCouponCode(Context context) {
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, TkpdCache.CACHE_PROMO_CODE);
        localCacheHandler.clearCache(TkpdCache.Key.KEY_CACHE_PROMO_CODE);
    }

    public static void addValueToCache(Context context, String store, String key, String value){
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, store);
        localCacheHandler.putString(key, value);
    }

    public static void removeValueFromCache(Context context, String store, String key){
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, store);
        localCacheHandler.clearCache(key);
    }

    public static String getValueFromCache(Context context, String store, String key){
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(context, store);
        return localCacheHandler.getString(key);
    }

}
