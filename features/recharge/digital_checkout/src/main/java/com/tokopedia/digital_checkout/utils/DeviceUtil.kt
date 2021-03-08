package com.tokopedia.digital_checkout.utils

import android.content.Context
import android.os.Build
import android.text.TextUtils
import com.tokopedia.common_digital.cart.data.entity.requestbody.RequestBodyIdentifier
import com.tokopedia.config.GlobalConfig
import com.tokopedia.digital_checkout.data.request.RequestBodyCheckout
import com.tokopedia.user.session.UserSession
import java.net.Inet4Address
import java.net.NetworkInterface
import java.net.SocketException

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
                return "127.0.0.1"
            }
            return "127.0.0.1"
        }

    private val deviceName: String
        private get() {
            val manufacturer = Build.MANUFACTURER ?: ""
            val model = Build.MODEL ?: ""
            return if (model.startsWith(manufacturer)) {
                capitalize(model)
            } else capitalize(manufacturer) + " " + model
        }

    private fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }
        return phrase.toString()
    }

    val userAgentForApiCall: String
        get() = ("Android Tokopedia Application/"
                + GlobalConfig.getPackageApplicationName()
                + " v." + GlobalConfig.VERSION_NAME
                + " (" + deviceName
                + "; Android; API_"
                + Build.VERSION.SDK_INT
                + "; Version"
                + Build.VERSION.RELEASE + ") ")

    fun getDigitalIdentifierParam(context: Context): RequestBodyIdentifier {
        val requestBodyIdentifier = RequestBodyIdentifier()
        val userSession = UserSession(context)
        requestBodyIdentifier.deviceToken = userSession.deviceId
        requestBodyIdentifier.userId = userSession.userId
        requestBodyIdentifier.osType = "1"
        return requestBodyIdentifier
    }

    fun getAppsFlyerIdentifierParam(afUniqueId: String?, adsId: String?): RequestBodyCheckout.RequestBodyAppsFlyer {
        val requestBodyAppsFlyer: RequestBodyCheckout.RequestBodyAppsFlyer = RequestBodyCheckout.RequestBodyAppsFlyer()
        requestBodyAppsFlyer.appsflyerId = afUniqueId ?: ""
        requestBodyAppsFlyer.deviceId = adsId ?: ""
        return requestBodyAppsFlyer
    }
}
