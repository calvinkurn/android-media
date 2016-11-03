package com.tokopedia.tkpd.prototype;

import android.app.Activity;
import android.content.Context;

public class InboxCache{
	
	public static String CacheName = "INBOX";
	
	public static void ClearCache(Activity context){
		MainCacheSystem.ClearCache(CacheName, context);
	}
	
	public static void ClearCache(Context context){
		MainCacheSystem.ClearCache(CacheName, context);
	}
	
	public static void DeleteCache(String CacheID, Activity context){
		MainCacheSystem.DeleteCache(CacheName, CacheID, context);
	}
	
	public static void DeleteCache(String CacheID, Context context){
		MainCacheSystem.DeleteCache(CacheName, CacheID, context);
	}
}