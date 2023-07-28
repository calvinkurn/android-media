package com.tokopedia.installreferral

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import com.tokopedia.iris.IrisAnalytics
import java.lang.Exception
import java.net.URLDecoder
import java.util.HashMap

object InstallUtils {
    val UTM_SOURCE = "utm_source"
    val UTM_MEDIUM = "utm_medium"
    val UTM_CAMPAIGN = "utm_campaign"
    val UTM_CONTENT = "utm_content"
    val UTM_TERM = "utm_term"
    val UTM_GCLID = "gclid"
    val X_CLID = "xClientId"
    val X_ATTR = "xAttribution"

    val IRIS_ANALYTICS_EVENT_KEY = "event"
    val IRIS_ANALYTICS_APP_INSTALL = "appInstall"
    val INSTALL_REFERRAL_ACTION="com.android.vending.INSTALL_REFERRER"


    fun isValidCampaignUrl(uri: Uri): Boolean {
        val maps = splitquery(uri)
        return maps.containsKey(UTM_GCLID) || maps.containsKey(UTM_SOURCE) && maps.containsKey(UTM_MEDIUM) && maps.containsKey(UTM_CAMPAIGN)
    }

    fun splitquery(url: Uri): Map<String, String> {
        val querypairs = LinkedHashMap<String, String>()
        val query = url.query
        if (!TextUtils.isEmpty(query)) {
            val pairs = query!!.split("&|\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (pair in pairs) {
                val indexkey = pair.indexOf("=")
                if (indexkey > 0 && indexkey + 1 <= pair.length) {
                    try {
                        querypairs[URLDecoder.decode(pair.substring(0, indexkey), "utf-8")] = URLDecoder.decode(pair.substring(indexkey + 1), "utf-8")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        }
        return querypairs
    }


    fun sendIrisInstallEvent(context: Context?) {
        if(context!= null) {
            val map = HashMap<String, Any>()
            map[InstallUtils.IRIS_ANALYTICS_EVENT_KEY] = InstallUtils.IRIS_ANALYTICS_APP_INSTALL
            IrisAnalytics.getInstance(context).sendEvent(map)
        }
    }
}


