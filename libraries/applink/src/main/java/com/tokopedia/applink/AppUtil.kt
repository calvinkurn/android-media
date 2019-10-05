package com.tokopedia.applink

import android.content.Context

/**
 * Author errysuprayogi on 11,September,2019
 */
object AppUtil {

    fun isSellerInstalled(context: Context?): Boolean {
        val topadsIntent = context?.let {
            it.getPackageManager().getLaunchIntentForPackage("com.tokopedia.sellerapp")
        }
        return topadsIntent != null
    }
}
