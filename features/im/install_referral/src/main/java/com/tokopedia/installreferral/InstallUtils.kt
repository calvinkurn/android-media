package com.tokopedia.installreferral

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import com.tokopedia.iris.IrisAnalytics
import java.net.URLDecoder


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
    val INSTALL_REFERRAL_ACTION = "com.android.vending.INSTALL_REFERRER"


    fun isValidCampaignUrl(uri: Uri): Boolean {
        val maps = splitquery(uri)
        return maps.containsKey(UTM_GCLID) || maps.containsKey(UTM_SOURCE) && maps.containsKey(
            UTM_MEDIUM
        ) && maps.containsKey(UTM_CAMPAIGN)
    }

    fun splitquery(url: Uri): Map<String, String> {
        val querypairs = LinkedHashMap<String, String>()
        val query = url.query
        if (!TextUtils.isEmpty(query)) {
            val pairs =
                query!!.split("&|\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (pair in pairs) {
                val indexkey = pair.indexOf("=")
                if (indexkey > 0 && indexkey + 1 <= pair.length) {
                    try {
                        querypairs[URLDecoder.decode(pair.substring(0, indexkey), "utf-8")] =
                            URLDecoder.decode(pair.substring(indexkey + 1), "utf-8")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }
        }
        return querypairs
    }


    fun sendIrisInstallEvent(context: Context?, installReferrer: String?) {
        if (context != null) {
            val map = HashMap<String, Any>()
            map[InstallUtils.IRIS_ANALYTICS_EVENT_KEY] = InstallUtils.IRIS_ANALYTICS_APP_INSTALL
            val referrer = installReferrer ?: ""
            val mapQuery = splitQuery(referrer)
            if (mapQuery.containsKey(UTM_SOURCE) && mapQuery.containsKey(UTM_MEDIUM)) {
                map[UTM_SOURCE] = mapQuery[UTM_SOURCE] ?: ""
                map[UTM_MEDIUM] = mapQuery[UTM_MEDIUM] ?: ""
            } else {
                map[UTM_SOURCE] = referrer
                map[UTM_MEDIUM] = "preload"
            }
            IrisAnalytics.getInstance(context).sendEvent(map)
        }
    }

    private fun splitQuery(query: String): Map<String, String> {
        val queryPairs: MutableMap<String, String> = HashMap()
        val pairs = query.split("&".toRegex()).dropLastWhile { it.isEmpty() }
        for (pair in pairs) {
            val idx = pair.indexOf("=")
            if (idx > 0) {
                queryPairs[pair.substring(0, idx)] = pair.substring(idx + 1)
            }
        }
        return queryPairs
    }
}


