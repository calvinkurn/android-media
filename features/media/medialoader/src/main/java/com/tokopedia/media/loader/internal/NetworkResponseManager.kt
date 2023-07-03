package com.tokopedia.media.loader.internal

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import okhttp3.Headers

class NetworkResponseManager constructor(
    context: Context
) : LocalCacheHandler(context, PREF_NAME) {

    fun set(header: Headers) {
        val mHeader = header.toMultimap().toString()

        putString(KEY_NETWORK_HEADER, mHeader)
        applyEditor()
    }

    fun header(): String = getString(KEY_NETWORK_HEADER, "")

    companion object {
        private const val PREF_NAME = "media_loader_network_response"

        private const val KEY_NETWORK_HEADER = "network_log_header"

        private var manager: NetworkResponseManager? = null

        fun getInstance(context: Context): NetworkResponseManager {
            return manager ?: NetworkResponseManager(context).also {
                manager = it
            }
        }
    }
}
