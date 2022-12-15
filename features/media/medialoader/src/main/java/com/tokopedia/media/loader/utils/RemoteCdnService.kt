package com.tokopedia.media.loader.utils

import com.tokopedia.abstraction.common.utils.network.AuthUtil.HEADER_USER_AGENT
import com.tokopedia.network.authentication.AuthHelper
import com.tokopedia.user.session.UserSession
import java.net.HttpURLConnection
import java.net.InetAddress
import java.net.URL
import java.net.UnknownHostException

object RemoteCdnService {
    @Throws(UnknownHostException::class, SecurityException::class)
    suspend fun fetchServerInfo(urlRemote: String): InetAddress {
        return InetAddress.getByName(URL(urlRemote).host)
    }

    suspend fun getCdnName(urlRemote: String): String {
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
        return cdnNameHeader
    }

    private fun HttpURLConnection.setRequestProperty(): HttpURLConnection {
        val methodGet = "GET"
        requestMethod = methodGet
        useCaches = false
        setRequestProperty(HEADER_USER_AGENT, AuthHelper.getUserAgent())
        return this
    }
}
