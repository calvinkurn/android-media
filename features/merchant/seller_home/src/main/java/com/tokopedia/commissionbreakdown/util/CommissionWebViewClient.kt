package com.tokopedia.commissionbreakdown.util

import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import okhttp3.OkHttpClient
import okhttp3.Request

/**
 * Created by @ilhamsuaib on 18/05/22.
 */

object CommissionWebViewClient {

    const val HEADER_AUTHORIZATION = "Accounts-Authorization"
    const val HEADER_ORIGIN = "Origin"
    const val ORIGIN_TOKOPEDIA = "tokopedia.com"

    private const val UTF_8 = "utf-8"
    private const val HEADER_CONTENT_ENCODING = "content-encoding"
    private const val MIME_TYPE = "application/octet-stream"

    fun getWebViewClient(userAuthorization: String, onError: (Exception) -> Unit): WebViewClient {
        return object : WebViewClient() {
            override fun shouldInterceptRequest(
                view: WebView?,
                request: WebResourceRequest
            ): WebResourceResponse? {
                val url = request.url.toString()
                return getResponse(url, userAuthorization, onError)
            }
        }
    }

    private fun getResponse(
        url: String,
        userAuthorization: String,
        onError: (Exception) -> Unit
    ): WebResourceResponse? {
        return try {
            val httpClient = OkHttpClient()
            val request: Request = Request.Builder()
                .url(url)
                .addHeader(HEADER_ORIGIN, ORIGIN_TOKOPEDIA)
                .addHeader(HEADER_AUTHORIZATION, userAuthorization)
                .build()
            val response = httpClient.newCall(request).execute()

            WebResourceResponse(
                MIME_TYPE,
                response.header(HEADER_CONTENT_ENCODING, UTF_8),
                response.body?.byteStream()
            )
        } catch (e: Exception) {
            onError(e)
            null
        }
    }
}