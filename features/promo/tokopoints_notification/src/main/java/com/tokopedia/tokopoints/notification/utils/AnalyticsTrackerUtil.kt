package com.tokopedia.tokopoints.notification.utils

import android.content.Context
import android.util.Log
import com.tokopedia.track.TrackApp
import com.tokopedia.track.TrackAppUtils
import timber.log.Timber

object AnalyticsTrackerUtil {
    object EventKeys {
            const val EVENT = "event"
            const val EVENT_CATEGORY = "eventCategory"
            const val EVENT_ACTION = "eventAction"
            const val EVENT_LABEL = "eventLabel"
            const val ECOMMERCE = "ecommerce"
            const val EVENT_CLICK_COUPON = "clickCoupon"
    }

    object CategoryKeys {
            const val POPUP_TERIMA_HADIAH = "pop up terima hadiah kupon"
    }

    object ActionKeys {
            const val CLICK_CLOSE_BUTTON = "click close button"
            const val CLICK_GUNAKAN_KUPON = "click gunakan kupon"
    }

        fun sendEvent(context:Context, event:String,category:String,
                      action:String,label:String){
            TrackApp.getInstance().gtm.sendGeneralEvent(TrackAppUtils.gtmData(event, category, action, label))
        }
}