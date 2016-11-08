package com.tokopedia.core.prototype;

import android.app.Activity;
import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;

public class ShopCache {

	public static void DeleteCache(String ShopID, Activity context){
		LocalCacheHandler.clearSingleCacheKey(context, "SHOP_CACHE", "S_EXPIRY_" + ShopID);
		LocalCacheHandler.clearSingleCacheKey(context, "SHOP_CACHE", "S_DATA_" + ShopID);
	}
	
	public static void ClearCache(Context context){
		LocalCacheHandler.clearCache(context, "SHOP_CACHE");
	}
}