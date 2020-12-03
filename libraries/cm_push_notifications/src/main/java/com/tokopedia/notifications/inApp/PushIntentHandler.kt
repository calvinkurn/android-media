package com.tokopedia.notifications.inApp

import android.app.Activity
import android.os.Bundle

class PushIntentHandler {

    var isHandledByPush = false

    fun processPushIntent(activity: Activity, bundle: Bundle?):Boolean {
        var isHandled = false
        for (pushHandler in getPushIntentHandlerList()) {
            val tempIsHandled =  pushHandler.processPushIntent(activity, bundle)
            if (tempIsHandled) {
                isHandled = tempIsHandled
            }
            break
        }
        return isHandled
    }

    private fun getPushIntentHandlerList() = CmEventListener.pushIntentContractList
}

interface PushIntentContract {
    fun processPushIntent(activity: Activity, bundle: Bundle?): Boolean
    fun isPushIntentHandled(bundle: Bundle?): Boolean
}