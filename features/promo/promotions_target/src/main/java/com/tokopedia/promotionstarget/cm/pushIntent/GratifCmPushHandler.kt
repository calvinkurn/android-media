package com.tokopedia.promotionstarget.cm.pushIntent

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.notifications.inApp.PushIntentContract
import com.tokopedia.promotionstarget.cm.dialog.GratificationDialogHandler

const val EXTRA_BASE_MODEL = "extra_base_model"
const val GRATIFICATION_ID = "gratificationId"

class GratifCmPushHandler(private val gratificationDialogHandler: GratificationDialogHandler) : PushIntentContract {

    override fun processPushIntent(activity: Activity, bundle: Bundle?): Boolean {
        if (gratificationDialogHandler.isGratifDisabled()) {
            return false
        }
        val isIntentHandled = isPushIntentHandled(bundle)
        if (isIntentHandled) {
            gratificationDialogHandler.showPushDialog(activity, getGratificationId(bundle), activity.javaClass.name)
        }
        return isIntentHandled
    }

    private fun getGratificationId(bundle: Bundle?): String {
        var gratificationId = ""
        if (bundle != null) {
            val isComingFromPush = bundle.keySet().contains(EXTRA_BASE_MODEL)
            if (isComingFromPush) {
                gratificationId = bundle.getString(GRATIFICATION_ID) ?: ""
            }
        }
        return gratificationId
    }

    override fun isPushIntentHandled(bundle: Bundle?): Boolean {
        var canShowPopupFromPush = false
        if (bundle != null) {
            val isComingFromPush = bundle.keySet().contains(EXTRA_BASE_MODEL)
            if (isComingFromPush) {
                val gratificationId = bundle.getString(GRATIFICATION_ID)
                canShowPopupFromPush = !TextUtils.isEmpty(gratificationId)
            }
        }
        return canShowPopupFromPush
    }
}