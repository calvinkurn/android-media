package com.tokopedia.topchat.chatroom.util

import android.net.Uri
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

class TopChatRoomRedirectionUtil @Inject constructor(private val client: OkHttpClient) {

    fun getRedirectionUrl(
        originalApplink: String,
        onStart: () -> Unit,
        onCompleted: (String) -> Unit
    ) {
        val urlString = Uri.parse(originalApplink).getQueryParameter(QUERY_PARAM_URL_KEY)
        if (urlString != null) {
            onStart()
            val request = Request.Builder().url(urlString).build()
            client.newCall(request).enqueue(getCallback(originalApplink, onCompleted))
        } else {
            // If not param URL, use original applink to open the page
            onCompleted(originalApplink)
        }
    }

    private fun getCallback(
        originalApplink: String,
        onCompleted: (String) -> Unit
    ): Callback {
        return object : Callback {
            override fun onResponse(call: Call, response: Response) {
                /**
                 * Only get the redirection URL when the error code is redirection
                 * Else, use original applink to open the page
                 */
                var redirectionUrl = originalApplink
                if (response.code == REDIRECTION_CODE) {
                    redirectionUrl = response.header(HEADER_LOCATION_KEY)?: originalApplink
                }
                onCompleted(redirectionUrl)
            }

            override fun onFailure(call: Call, e: IOException) {
                Timber.d(e)
                // If fail, use original applink to open the page
                onCompleted(originalApplink)
            }
        }
    }

    companion object {
        private const val QUERY_PARAM_URL_KEY = "url"
        private const val HEADER_LOCATION_KEY = "Location"
        private const val REDIRECTION_CODE = 307
    }
}
