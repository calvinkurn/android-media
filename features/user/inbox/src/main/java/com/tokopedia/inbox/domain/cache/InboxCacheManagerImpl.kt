package com.tokopedia.inbox.domain.cache

import android.content.SharedPreferences
import com.tokopedia.common.network.util.CommonUtil
import java.lang.reflect.Type
import javax.inject.Inject

class InboxCacheManagerImpl @Inject constructor(
        private val sharedPreferences: SharedPreferences
) : InboxCacheManager {

    override fun saveCache(key: String, obj: Any) {
        val cacheString = CommonUtil.toJson(obj)
        sharedPreferences.edit()
                .putString(key, cacheString)
                .apply()
    }

    override fun saveCacheInt(key: String, int: Int) {
        sharedPreferences.edit()
                .putInt(key, int)
                .apply()
    }

    override fun <T> loadCache(key: String, type: Type): T? {
        return try {
            val cacheString = sharedPreferences.getString(key, "")
            CommonUtil.fromJson(cacheString, type)
        } catch (e: Exception) {
            null
        }
    }

    override fun loadCacheInt(key: String): Int? {
        return try {
            return sharedPreferences.getInt(key, -1)
        } catch (e: Exception) {
            null
        }
    }

    override fun saveCacheBoolean(key: String, bool: Boolean) {
        sharedPreferences.edit()
                .putBoolean(key, bool)
                .apply()
    }

    override fun loadCacheBoolean(key: String): Boolean? {
        return try {
            return sharedPreferences.getBoolean(key, false)
        } catch (e: Exception) {
            null
        }
    }

}