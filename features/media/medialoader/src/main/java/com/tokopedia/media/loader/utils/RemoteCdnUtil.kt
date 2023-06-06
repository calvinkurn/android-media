package com.tokopedia.media.loader.utils

import android.util.Patterns
import android.webkit.URLUtil
import com.tokopedia.abstraction.common.utils.network.AuthUtil.HEADER_USER_AGENT
import com.tokopedia.network.authentication.AuthHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL
import java.net.UnknownHostException

internal object RemoteCdnUtil {

    @Throws(UnknownHostException::class, SecurityException::class)
    suspend fun fetchServerInfo(urlRemote: String): InetAddress {
        return withContext(Dispatchers.IO) {
            InetAddress.getByName(URL(urlRemote).host)
        }
    }

    suspend fun getCdnName(urlRemote: String): String {
        return withContext(Dispatchers.IO) {
            var urlConnection: HttpURLConnection? = null
            val url: URL

            val cdnHeaderKey = "x-tkpd-cdn-name"
            var cdnNameHeader = ""

            try {
                url = URL(urlRemote)
                urlConnection = url.openConnection() as? HttpURLConnection
                urlConnection = urlConnection?.setRequestProperty()
                cdnNameHeader = urlConnection?.getHeaderField(cdnHeaderKey).orEmpty()
            } catch (e: Throwable) {
                e.printStackTrace()
            } finally {
                urlConnection?.disconnect()
            }

            cdnNameHeader
        }
    }

    fun isValidUrl(urlString: String?): Boolean {
        if (urlString.isNullOrBlank()) return false

        val urlRegex = "^(https?)://.*\$".toRegex()

        return try {
            (URLUtil.isValidUrl(urlString) && Patterns.WEB_URL.matcher(urlString).matches()) ||
                urlString.matches(urlRegex)
        } catch (ignored: Exception) {
            false
        }
    }

    private fun HttpURLConnection.setRequestProperty(): HttpURLConnection {
        val methodGet = "GET"
        requestMethod = methodGet
        useCaches = false
        setRequestProperty(HEADER_USER_AGENT, AuthHelper.getUserAgent())
        return this
    }
}
