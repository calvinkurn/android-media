package com.tokopedia.core.prototype;

import android.app.Activity;
import android.content.Context;

import com.tkpd.library.utils.LocalCacheHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class ShopSettingCache {
	
	public static String CODE_ETALASE = "etalase";
	public static String CODE_PROFILE = "user_profile";
	public static String CODE_NOTIFICATION = "notif";
	public static String CODE_ADDRESS = "address";
	public static String CODE_PRIVACY = "privacy";
	public static String CODE_SHOP_INFO = "shop_info";
	public static String CODE_PAYMENT = "payment";
	public static String CODE_NOTES = "notes";

	public static JSONObject getSetting(String SettingID, Activity context){
		return getSetting(SettingID, (Context)context);
	}
	
	public static JSONObject getSetting(String SettingID, Context context){
		LocalCacheHandler cache = new LocalCacheHandler(context, "SETTING_CACHE");
		Long createTime = cache.getLong("C_EXPIRY_" + SettingID);
		try {
			if(((System.currentTimeMillis()/1000) - createTime) < 300){
				return new JSONObject(cache.getString("C_DATA_" + SettingID));
			}
			else
				return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void DeleteCache(String SettingID, Activity context){
		LocalCacheHandler.clearSingleCacheKey(context, "SETTING_CACHE", "C_EXPIRY_" + SettingID);
		LocalCacheHandler.clearSingleCacheKey(context, "SETTING_CACHE", "C_DATA_" + SettingID);
	}
	
	public static void DeleteCache(String SettingID, Context context){
		LocalCacheHandler.clearSingleCacheKey(context, "SETTING_CACHE", "C_EXPIRY_" + SettingID);
		LocalCacheHandler.clearSingleCacheKey(context, "SETTING_CACHE", "C_DATA_" + SettingID);
	}
	
	// Untuk nyimpan toko yang baru diload
	public static void SaveCache(String SettingID, String HotData,Activity context){
		SaveCache(SettingID, HotData, (Context)context);
	}
	
	public static void SaveCache(String SettingID, String HotData,Context context){
		Long createTime = System.currentTimeMillis()/1000;
		LocalCacheHandler cache = new LocalCacheHandler(context, "SETTING_CACHE");
		cache.putLong("C_EXPIRY_" + SettingID, createTime);
		cache.putString("C_DATA_" + SettingID, HotData);
		cache.applyEditor();
	}
	
	public static void ClearCache(Context context){
		LocalCacheHandler.clearCache(context, "SETTING_CACHE");
	}
}
