package com.tokopedia.core.prototype;

import android.content.Context;

public class PembelianCache {

	public static String CACHENAME = "Pembelian";
	
	public static void ClearCache(Context context){
		MainCacheSystem.ClearCache(CACHENAME, context);
	}
}
