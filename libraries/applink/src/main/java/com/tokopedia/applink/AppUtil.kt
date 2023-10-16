package com.tokopedia.applink

import android.app.Activity
import android.content.Context
import java.lang.ref.WeakReference

/**
 * Author errysuprayogi on 11,September,2019
 */
object AppUtil {

    var currentActivityReference: WeakReference<Activity>? = null

    fun isSellerInstalled(context: Context?): Boolean {
        val topadsIntent = context?.packageManager?.getLaunchIntentForPackage("com.tokopedia.sellerapp")
        return topadsIntent != null
    }

}
