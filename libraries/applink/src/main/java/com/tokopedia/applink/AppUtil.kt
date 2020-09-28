package com.tokopedia.applink

import android.content.Context
import android.net.Uri
import timber.log.Timber

/**
 * Author errysuprayogi on 11,September,2019
 */
object AppUtil {
    val alreadyLoggedList = mutableSetOf<String>()

    fun isSellerInstalled(context: Context?): Boolean {
        val topadsIntent = context?.let {
            it.getPackageManager().getLaunchIntentForPackage("com.tokopedia.sellerapp")
        }
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
                Timber.w("P1#AIRBNB_OPEN#%s", hostAndPath)
            }
        } catch (e:Exception) {

        }
    }
}
