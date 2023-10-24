package com.tokopedia.common.network.cdn

import okhttp3.Dns
import java.net.InetAddress
import java.util.Arrays

class CdnDns(var cname: String) : Dns {

    override fun lookup(hostname: String): List<InetAddress> {
        if (cname.isBlank()) cname = hostname
        val addr = InetAddress.getByName(cname)
        val address = Arrays.asList(addr)
        return address
    }
}
