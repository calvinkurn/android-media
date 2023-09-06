package com.tokopedia.tokochat.common.util

import android.content.SharedPreferences
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

    override fun <T> loadCache(key: String, type: Class<T>): T? {
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

    companion object {
        const val TOKOCHAT_IMAGE_ATTACHMENT_MAP = "image_attachment_map"
    }
}
