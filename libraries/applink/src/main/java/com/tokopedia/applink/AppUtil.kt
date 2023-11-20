package com.tokopedia.applink

import android.app.Activity
import android.content.Context
import android.content.Intent
import java.lang.ref.WeakReference

/**
 * Author errysuprayogi on 11,September,2019
 */
object AppUtil {

    var currentActivityReference: WeakReference<Activity>? = null

    fun startActivityFromCurrentActivity(intent: Intent): Boolean {
        val reference = currentActivityReference
        if (reference != null) {
            val activity = reference.get()
            if (activity != null) {
                activity.startActivity(intent)
                return true
            }
        }
        return false
    }

    fun isSellerInstalled(context: Context?): Boolean {
        val topadsIntent =
            context?.packageManager?.getLaunchIntentForPackage("com.tokopedia.sellerapp")
        return topadsIntent != null
    }

}
