package com.tokopedia.notifications.inApp

import android.app.Activity
import android.os.Bundle

class PushIntentHandler {

    fun checkPushIntent(activity: Activity, bundle: Bundle?) {
        for (pushHandler in getPushIntentHandlerList()) {
            pushHandler.checkPushIntent(activity, bundle)
        }
    }

    private fun getPushIntentHandlerList() = CmEventListener.pushIntentContractList
}

interface PushIntentContract {
    fun checkPushIntent(activity: Activity, bundle: Bundle?)
}