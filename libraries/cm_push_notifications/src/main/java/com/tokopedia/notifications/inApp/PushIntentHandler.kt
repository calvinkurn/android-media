package com.tokopedia.notifications.inApp

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.notifications.common.CMConstant
import timber.log.Timber

class PushIntentHandler (private val gratificationDialogHandler: GratificationDialogHandler){

    fun checkPushIntent(activity: Activity, bundle: Bundle?){
        var canShowPopupFromPush = false
        var gratificationId: String? = null
        if (bundle != null) {
            val isComingFromPush = bundle.keySet().contains(CMConstant.EXTRA_BASE_MODEL)
            if (isComingFromPush) {
                gratificationId = bundle.getString("gratificationId") //todo Remove hardcoding
                canShowPopupFromPush = !TextUtils.isEmpty(gratificationId)
                try {
                    val intent = activity.intent
                    val activityBundle = intent.extras
                    if (activityBundle != null) {
                        if (activityBundle.keySet().contains(CMConstant.EXTRA_BASE_MODEL)) {
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
            gratificationDialogHandler.showPushDialog(activity, gratificationId)
        }
    }
}