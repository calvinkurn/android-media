package com.tokopedia.instantdebitbca.data.view.utils

import android.os.Build

import com.tokopedia.abstraction.common.utils.GlobalConfig

import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.Enumeration

/**
 * Created by nabillasabbaha on 27/03/19.
 */
object DeviceUtil {

    val localIpAddress: String
        get() {
            try {
                val en = NetworkInterface.getNetworkInterfaces()
                while (en.hasMoreElements()) {
                    val intf = en.nextElement()
                    val enumIpAddr = intf.inetAddresses
                    while (enumIpAddr.hasMoreElements()) {
                        val inetAddress = enumIpAddr.nextElement()
                        if (!inetAddress.isLoopbackAddress && inetAddress is Inet4Address) {
                            return inetAddress.getHostAddress()
                        }
                    }
                }
            } catch (ex: SocketException) {
                ex.printStackTrace()
                return "127.0.0.1"
            }

            return "127.0.0.1"
        }

    private val userAgentFormat = "TkpdConsumer/%s (%s;)"

    val userAgent: String
        get() = String.format(userAgentFormat, GlobalConfig.VERSION_NAME, "Android " + Build.VERSION.RELEASE)
}
