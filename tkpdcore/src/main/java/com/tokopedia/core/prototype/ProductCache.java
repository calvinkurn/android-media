package com.tokopedia.core.prototype;

import android.app.Activity;
import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;

public class ProductCache {

	public static void DeleteCache(String ProductID, Context context){
		LocalCacheHandler.clearSingleCacheKey(context, "PRODUCT_CACHE", "P_EXPIRY_" + ProductID);
		LocalCacheHandler.clearSingleCacheKey(context, "PRODUCT_CACHE", "P_DATA_" + ProductID);
	}

	public static void SetPermission(String ProdID, String IsProdManager, Context context){
		LocalCacheHandler cache = new LocalCacheHandler(context, "PRODUCT_CACHE");
		cache.putString("P_PERMISSION_" + ProdID, IsProdManager);
		cache.applyEditor();
	}
	
	public static void ClearCache(Activity context){
		LocalCacheHandler.clearCache(context, "PRODUCT_CACHE");
	}
	
	public static void ClearCache(Context context){
		LocalCacheHandler.clearCache(context, "PRODUCT_CACHE");
	}
}