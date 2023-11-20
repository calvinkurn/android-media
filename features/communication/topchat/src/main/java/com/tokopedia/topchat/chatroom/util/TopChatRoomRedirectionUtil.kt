package com.tokopedia.topchat.chatroom.util

import android.net.Uri
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class TopChatRoomRedirectionUtil @Inject constructor(
    private val client: OkHttpClient,
    private val dispatcher: CoroutineDispatchers
) {

    suspend fun getRedirectionUrl(originalApplink: String): String {
        val urlString = Uri.parse(originalApplink).getQueryParameter(QUERY_PARAM_URL_KEY)
        var redirectionUrl = originalApplink // Set redirection to original applink first
        if (urlString != null) {
            val request = Request.Builder().url(urlString).build()
            val response = makeRequest(request)
            // If the response is 307 (redirection), get the location value
            // else use original applink
            if (response.code == REDIRECTION_CODE) {
                redirectionUrl = response.header(HEADER_LOCATION_KEY) ?: originalApplink
            }
        }
        return redirectionUrl
    }

    private suspend fun makeRequest(request: Request): Response {
        return withContext(dispatcher.io) {
            client.newCall(request).execute()
        }
    }

    companion object {
        private const val QUERY_PARAM_URL_KEY = "url"
        private const val HEADER_LOCATION_KEY = "Location"
        private const val REDIRECTION_CODE = 307
    }
}
