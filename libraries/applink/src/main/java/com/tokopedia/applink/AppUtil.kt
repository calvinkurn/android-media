package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority

/**
 * Author errysuprayogi on 11,September,2019
 */
object AppUtil {
    val alreadyLoggedList = mutableSetOf<String>()

    fun isSellerInstalled(context: Context?): Boolean {
        val topadsIntent = context?.packageManager?.getLaunchIntentForPackage("com.tokopedia.sellerapp")
        return topadsIntent != null
    }

    @JvmStatic
    fun logAirBnbUsage(applinkUri: String) {
        try {
            val uri = Uri.parse(applinkUri)
            logAirBnbUsage(uri)
        } catch (e:Exception) {

        }
    }

    @JvmStatic
    fun logAirBnbUsage(uri: Uri) {
        try {
            val hostAndPath = uri.host + uri.path
            if (!alreadyLoggedList.contains(hostAndPath)) {
                alreadyLoggedList.add(hostAndPath)
                ServerLogger.log(Priority.P1, "AIRBNB_OPEN", mapOf("type" to hostAndPath))
            }
        } catch (e:Exception) {

        }
    }
}
