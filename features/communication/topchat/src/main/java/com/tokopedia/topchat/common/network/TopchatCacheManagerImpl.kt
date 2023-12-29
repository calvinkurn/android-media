package com.tokopedia.topchat.common.network

import android.content.SharedPreferences
import com.tokopedia.common.network.util.CommonUtil
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TopchatCacheManagerImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : TopchatCacheManager {

    override fun saveCache(key: String, obj: Any) {
        val cacheString = CommonUtil.toJson(obj)
        sharedPreferences.edit()
            .putString(key, cacheString)
            .apply()
    }

    override fun <T> loadCache(key: String, type: Type): T {
        val cacheString = sharedPreferences.getString(key, "")
        return CommonUtil.fromJson(cacheString, type)
    }

    override fun saveState(stateCacheKey: String, value: Boolean) {
        sharedPreferences.edit()
            .putBoolean(stateCacheKey, value)
            .apply()
    }

    override fun getPreviousState(stateCacheKey: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(stateCacheKey, defaultValue)
    }

    override fun saveLongCache(cacheKey: String, value: Long) {
        sharedPreferences.edit()
            .putLong(cacheKey, value)
            .apply()
    }

    override fun getLongCache(cacheKey: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(cacheKey, defaultValue)
    }
}
