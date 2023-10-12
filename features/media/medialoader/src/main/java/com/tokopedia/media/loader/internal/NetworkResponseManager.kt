package com.tokopedia.media.loader.internal

import android.annotation.SuppressLint
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.media.loader.data.Header
import com.tokopedia.media.loader.data.toModel
import com.tokopedia.media.loader.isValid
import okhttp3.Headers

class NetworkResponseManager constructor(
    private val context: Context
) : LocalCacheHandler(context, PREF_NAME) {

    fun set(url: String, header: Headers) {
        if (header.size <= 0) return
        if (hasReachedThreshold()) forceResetCache()

        val mHeader = header
            .toMultimap()
            .toModel()
            .toJson()

        putString(url, mHeader)
        applyEditor()
    }

    fun header(url: String): List<Header> {
        if (context.isValid().not()) return emptyList()

        val header = getString(url, "")
        if (header.isEmpty()) return emptyList()

        return header.toModel()
    }

    fun forceResetCache() {
        clearCache()
    }

    private fun hasReachedThreshold(): Boolean {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val entrySize = sharedPref.all.size

        return entrySize == CACHE_THRESHOLD
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
        private const val PREF_NAME = "media_loader_network_response"
        private const val CACHE_THRESHOLD = 50 // 50 images cache limit

        @SuppressLint("StaticFieldLeak")
        @Volatile private var manager: NetworkResponseManager? = null

        fun getInstance(context: Context): NetworkResponseManager {
            return manager ?: synchronized(this) {
                NetworkResponseManager(context).also {
                    manager = it
                }
            }
        }
    }
}
