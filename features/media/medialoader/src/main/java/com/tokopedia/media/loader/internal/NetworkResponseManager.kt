package com.tokopedia.media.loader.internal

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.media.loader.data.Header
import com.tokopedia.media.loader.data.toModel
import okhttp3.Headers

class NetworkResponseManager constructor(
    context: Context
) : LocalCacheHandler(context, PREF_NAME) {

    fun set(header: Headers) {
        val mHeader = header
            .toMultimap()
            .toModel()
            .toJson()

        putString(KEY_NETWORK_HEADER, mHeader)
        applyEditor()
    }

    fun forceResetCache() {
        clearCache()
    }

    fun header(): List<Header> {
        val json = getString(KEY_NETWORK_HEADER, "")
        if (json.isEmpty()) return emptyList()

        return json.toModel()
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
        private const val KEY_NETWORK_HEADER = "network_log_header"

        @Volatile
        private var manager: NetworkResponseManager? = null

        fun getInstance(context: Context): NetworkResponseManager {
            return manager ?: NetworkResponseManager(context).also {
                manager = it
            }
        }
    }
}
