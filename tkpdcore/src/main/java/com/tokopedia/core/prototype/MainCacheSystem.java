package com.tokopedia.core.prototype;

import android.app.Activity;
import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @author Tkpd_Eka
 * call super. on all subclasses
 */
public abstract class MainCacheSystem {
	
	public static JSONObject getCache(String CacheName, String CacheID,int ExpiryInMS, Context context){
		LocalCacheHandler cache = new LocalCacheHandler(context, CacheName);
		Long createTime = cache.getLong(CacheName.charAt(0) + "_EXPIRY_" + CacheID);
		try {
			if(((System.currentTimeMillis()/1000) - createTime) < ExpiryInMS){
				return new JSONObject(cache.getString(CacheName.charAt(0) + "_DATA_" + CacheID));
			}
			else
				return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static JSONObject getCache(String CacheName, String CacheType, String CacheID,int ExpiryInMS, Activity context){
		return getCache(CacheName, CacheType, CacheID, ExpiryInMS, (Context)context);
	}
	
	public static JSONObject getCache(String CacheName, String CacheType, String CacheID,int ExpiryInMS, Context context){
		LocalCacheHandler cache = new LocalCacheHandler(context, CacheName);
		Long createTime = cache.getLong(CacheName.charAt(0) + "_EXPIRY_" + CacheID);
		try {
			if(((System.currentTimeMillis()/1000) - createTime) < ExpiryInMS){
				return new JSONObject(cache.getString(CacheName.charAt(0) + "_" + CacheType + "_" + CacheID));
			}
			else
				return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	public static void DeleteCache(String CacheName, String CacheID, Activity context){
		DeleteCache(CacheName, CacheID, (Context)context);
	}
	
	public static void DeleteCache(String CacheName, String CacheID, Context context){
		LocalCacheHandler.clearSingleCacheKey(context, CacheName, CacheName.charAt(0) + "_EXPIRY_" + CacheID);
		LocalCacheHandler.clearSingleCacheKey(context, CacheName, CacheName.charAt(0) + "_DATA_" + CacheID);
	}
	
	public static void SaveCache(String CacheName, String CacheID, String CacheData, Activity context){
		SaveCache(CacheName, CacheID, CacheData, (Context)context);
	};
	
	public static void SaveCache(String CacheName, String CacheID, String CacheData, Context context){
		Long createTime = System.currentTimeMillis()/1000;
		LocalCacheHandler cache = new LocalCacheHandler(context, CacheName);
		cache.putLong(CacheName.charAt(0) + "_EXPIRY_" + CacheID, createTime);
		cache.putString(CacheName.charAt(0) + "_DATA_" + CacheID, CacheData);
		cache.applyEditor();
	}

	public static void ClearCache(String CacheName, Activity context){
		LocalCacheHandler.clearCache(context, CacheName);
	}
	
	public static void ClearCache(String CacheName, Context context){
		LocalCacheHandler.clearCache(context, CacheName);
	}
}
