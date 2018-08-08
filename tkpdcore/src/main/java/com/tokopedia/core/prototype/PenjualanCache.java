package com.tokopedia.core.prototype;

import android.content.Context;

public class PenjualanCache {
	
	public static String CacheName = "SHOP_TX";

	public static void ClearCache(Context context){
		MainCacheSystem.ClearCache(CacheName, context);
	}
}
