package com.tokopedia.chatbot.data.cache

import javax.inject.Singleton
import java.lang.reflect.Type

@Singleton
interface ChatbotCacheManager {
    fun saveCache(key: String, data: Any)
    fun <T> loadCache(key: String, type: Type): T
    fun saveState(stateCacheKey: String, value: Boolean)
    fun loadPreviousState(stateCacheKey: String): Boolean
}