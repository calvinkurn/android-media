package com.tokopedia.promotionstarget.cmGratification.pushIntent

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.notifications.inApp.PushIntentContract
import com.tokopedia.promotionstarget.cmGratification.dialog.GratificationDialogHandler
import timber.log.Timber

class GratifCmPushHandler(val gratificationDialogHandler: GratificationDialogHandler) : PushIntentContract {

    override fun checkPushIntent(activity: Activity, bundle: Bundle?) {
        var canShowPopupFromPush = false
        var gratificationId: String? = null
        if (bundle != null) {
            //todo Rahul reomve comment
//            val isComingFromPush = bundle.keySet().contains(CMConstant.EXTRA_BASE_MODEL)
            val isComingFromPush = bundle.keySet().contains("extra_base_model")
            if (isComingFromPush) {
                gratificationId = bundle.getString("gratificationId") //todo Remove hardcoding
                canShowPopupFromPush = !TextUtils.isEmpty(gratificationId)
                try {
                    val intent = activity.intent
                    val activityBundle = intent.extras
                    if (activityBundle != null) {
                        //todo Rahul reomve comment
//                        if (activityBundle.keySet().contains(CMConstant.EXTRA_BASE_MODEL)) {
                        if (activityBundle.keySet().contains("extra_base_model")) {
                            activity.intent.removeExtra("gratificationId")
                            activity.intent.putExtra("gratificationId_processed", gratificationId)
                        }
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }
        if (canShowPopupFromPush && !gratificationId.isNullOrEmpty()) {
            gratificationDialogHandler.showPushDialog(activity, gratificationId, activity.javaClass.name)
        } else {
            gratificationDialogHandler.executePendingInApp(activity, activity.javaClass.name)
        }
    }
}