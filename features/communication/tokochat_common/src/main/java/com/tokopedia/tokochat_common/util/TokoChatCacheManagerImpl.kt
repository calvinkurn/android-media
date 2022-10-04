package com.tokopedia.tokochat_common.util

import android.content.SharedPreferences
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokoChatCacheManagerImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : TokoChatCacheManager {

    override fun saveCache(key: String, obj: Any) {
        val cacheString = CommonUtil.toJson(obj)
        sharedPreferences.edit()
            .putString(key, cacheString)
            .apply()
    }

    override fun <T> loadCache(key: String, type: Type): T? {
        val cacheString = sharedPreferences.getString(key, "")
        return CommonUtil.fromJson(cacheString, type)
    }

    override fun saveState(stateCacheKey: String, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(stateCacheKey, value)
            .apply()
    }

    override fun getPreviousState(stateCacheKey: String): Boolean {
        return sharedPreferences.getBoolean(stateCacheKey, false)
    }

}
