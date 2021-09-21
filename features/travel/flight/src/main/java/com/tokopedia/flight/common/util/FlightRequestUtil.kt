package com.tokopedia.flight.common.util

import android.os.Build
import com.tokopedia.config.GlobalConfig
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * @author by furqan on 10/06/2021
 */
object FlightRequestUtil {

    fun md5(str: String): String =
            try {
                val digest = MessageDigest.getInstance("MD5")
                digest.update(str.toByteArray())
                val messageDigest = digest.digest()

                val hexString = StringBuilder()
                for (byte in messageDigest) {
                    hexString.append(String.format("%02x", byte.toInt() and 0xff))
                }
                hexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                ""
            }

    fun getUserAgentForApiCall(): String =
            "Android Tokopedia Application/" +
                    "${GlobalConfig.getPackageApplicationName()} " +
                    "v.${GlobalConfig.VERSION_NAME} " +
                    "(${getDeviceName()}; " +
                    "Android; " +
                    "API_${Build.VERSION.SDK_INT}; " +
                    "Version${Build.VERSION.RELEASE}) "

    fun getLocalIpAddress(): String {
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
        } catch (e: SocketException) {
            e.printStackTrace()
            return "127.0.0.1"
        }
        return "127.0.0.1"
    }

    private fun getDeviceName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL

        if (manufacturer != null && model != null && model.startsWith(manufacturer)) {
            return capitalize(model)
        }
        return "${capitalize(manufacturer)} $model"
    }

    private fun capitalize(str: String?): String {
        str?.let {
            if (it.isEmpty()) return ""

            val arrChar = it.toCharArray()
            var capitalizeNext = true

            val phrase = StringBuilder()
            for (char in arrChar) {
                if (capitalizeNext && char.isLetter()) {
                    phrase.append(char.toUpperCase())
                    capitalizeNext = false
                    continue
                } else if (char.isWhitespace()) {
                    capitalizeNext = true
                }
                phrase.append(char)
            }

            return phrase.toString()
        }
        return ""
    }

}