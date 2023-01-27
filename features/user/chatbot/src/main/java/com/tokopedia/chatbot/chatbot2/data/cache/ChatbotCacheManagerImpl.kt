package com.tokopedia.chatbot.chatbot2.data.cache

import android.content.SharedPreferences
import com.tokopedia.common.network.util.CommonUtil
import java.lang.reflect.Type
import javax.inject.Inject

class ChatbotCacheManagerImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : ChatbotCacheManager {
    override fun saveCache(key: String, data: Any) {
        val cacheString = CommonUtil.toJson(data)
        sharedPreferences.edit().putString(key, cacheString).apply()
    }

    override fun <T> loadCache(key: String, type: Type): T {
        val cacheString = sharedPreferences.getString(key, "")
        return CommonUtil.fromJson(cacheString, type)
    }

    override fun saveState(stateCacheKey: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(stateCacheKey, value).apply()
    }

    override fun loadPreviousState(stateCacheKey: String): Boolean {
        return sharedPreferences.getBoolean(stateCacheKey, false)
    }
}
