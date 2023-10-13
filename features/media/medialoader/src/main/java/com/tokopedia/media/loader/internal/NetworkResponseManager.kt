package com.tokopedia.media.loader.internal

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.media.loader.data.Header
import com.tokopedia.media.loader.data.toModel
import okhttp3.Headers

class NetworkResponseManager private constructor() {

    private val headers = mutableMapOf<String, String>()

    fun set(url: String, header: Headers) {
        if (header.size <= 0) return
        if (hasReachedThreshold()) forceResetCache()

        headers[url] = header
            .toMultimap()
            .toModel()
            .toJson()
    }

    fun header(url: String): List<Header> {
        val header = headers[url] ?: return emptyList()

        return try {
            header.toModel()
        } catch (t: Throwable) {
            emptyList()
        }
    }

    fun forceResetCache() {
        headers.clear()
    }

    private fun hasReachedThreshold(): Boolean {
        return headers.size == CACHE_THRESHOLD
    }

    private fun List<Header>.toJson(): String {
        return Gson().toJson(this)
    }

    private fun String.toModel(): List<Header> {
        return Gson().fromJson(
            this,
            object : TypeToken<List<Header>>() {}.type
        )
    }

    companion object {
        private const val CACHE_THRESHOLD = 50 // 50 images cache limit

        @Volatile private var manager: NetworkResponseManager? = null

        fun getInstance(): NetworkResponseManager {
            return manager ?: synchronized(this) {
                NetworkResponseManager().also {
                    manager = it
                }
            }
        }
    }
}
