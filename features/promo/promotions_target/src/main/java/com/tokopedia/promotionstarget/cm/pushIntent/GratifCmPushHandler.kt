package com.tokopedia.promotionstarget.cm.pushIntent

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import com.tokopedia.notifications.inApp.PushIntentContract
import com.tokopedia.promotionstarget.cm.dialog.GratificationDialogHandler
import timber.log.Timber

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
        try {
            if (bundle != null) {
                val isComingFromPush = bundle.keySet().contains(EXTRA_BASE_MODEL)
                if (isComingFromPush) {
                    gratificationId = bundle.getString(GRATIFICATION_ID) ?: ""
                }
            }

        } catch (t: Throwable) {
            Timber.e(t)
        }
        return gratificationId
    }

    override fun isPushIntentHandled(bundle: Bundle?): Boolean {
        var canShowPopupFromPush = false
        try {
            if (bundle != null) {
                val isComingFromPush = bundle.keySet().contains(EXTRA_BASE_MODEL)
                if (isComingFromPush) {
                    val gratificationId = bundle.getString(GRATIFICATION_ID)
                    canShowPopupFromPush = !TextUtils.isEmpty(gratificationId)
                }
            }
        } catch (t: Throwable) {
            Timber.e(t)
        }
        return canShowPopupFromPush
    }
}