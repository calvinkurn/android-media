package com.tokopedia.utils.uri

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.content.res.AppCompatResources
import timber.log.Timber

object DeeplinkUtils {

    fun getReferrerCompatible(activity: Activity?): String {
        activity?.intent?.extras?.let { bundle ->
            val keySet = bundle.keySet().toTypedArray()
            for (key in keySet) {
                if (key.contains("application_id")) {
                    bundle.getString(key)?.let {
                        return it
                    }
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            activity?.referrer?.let {
                return it.toString()
            }
        }
        return ""
    }

    fun getExtraReferrer(activity: Activity?): Uri {
        activity?.intent?.extras?.let { bundle ->
            bundle.get("android.intent.extra.REFERRER")?.let {
                return it as Uri
            }
        }
        return Uri.EMPTY
    }

    fun getDataUri(activity: Activity?): Uri {
        activity?.intent?.data?.let {
            return it
        }
        return Uri.EMPTY
    }
}