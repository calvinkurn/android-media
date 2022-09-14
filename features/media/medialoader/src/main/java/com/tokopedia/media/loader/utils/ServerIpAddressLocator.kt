package com.tokopedia.media.loader.utils

import java.net.InetAddress
import java.net.URL
import java.net.UnknownHostException

class ServerIpAddressLocator {
    companion object {
        @Throws(UnknownHostException::class, SecurityException::class)
        suspend fun fetchServerInfo(urlRemote: String): InetAddress {
            return InetAddress.getByName(URL(urlRemote).host)
        }
    }
}