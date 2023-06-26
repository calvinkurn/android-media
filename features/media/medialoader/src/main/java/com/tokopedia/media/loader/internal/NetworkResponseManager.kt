package com.tokopedia.media.loader.internal

import android.content.Context
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import okhttp3.Headers
import okhttp3.Response

class NetworkResponseManager constructor(
    context: Context
) : LocalCacheHandler(context, PREF_NAME) {

    fun set(header: Headers, response: Response) {
        val mHeader = header.toMultimap().toString()
        val mResponse = response.body?.string()

        putString(KEY_NETWORK_HEADER, mHeader)
        putString(KEY_NETWORK_RESPONSE, mResponse)

        applyEditor()
    }

    fun header() = getString(KEY_NETWORK_HEADER, "")

    fun response() = getString(KEY_NETWORK_RESPONSE, "")

    companion object {
        private const val PREF_NAME = "media_loader_network_response"

        private const val KEY_NETWORK_HEADER = "network_log_header"
        private const val KEY_NETWORK_RESPONSE = "network_log_response"

        private var manager: NetworkResponseManager? = null

        fun getInstance(context: Context): NetworkResponseManager {
            return manager ?: NetworkResponseManager(context).also {
                manager = it
            }
        }
    }
}
