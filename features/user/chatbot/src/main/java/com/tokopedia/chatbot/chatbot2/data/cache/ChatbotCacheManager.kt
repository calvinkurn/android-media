package com.tokopedia.chatbot.chatbot2.data.cache

import java.lang.reflect.Type
import javax.inject.Singleton

@Singleton
interface ChatbotCacheManager {
    fun saveCache(key: String, data: Any)
    fun <T> loadCache(key: String, type: Type): T
    fun saveState(stateCacheKey: String, value: Boolean)
    fun loadPreviousState(stateCacheKey: String): Boolean
}
