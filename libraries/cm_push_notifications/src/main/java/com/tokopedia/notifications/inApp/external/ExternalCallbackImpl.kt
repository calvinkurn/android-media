package com.tokopedia.notifications.inApp.external

import android.app.Activity
import com.tokopedia.notifications.inApp.CMInAppManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp

internal class ExternalCallbackImpl(private val cmInAppManager: CMInAppManager)
    : IExternalInAppCallback {

    override fun onInAppViewShown(activity: Activity) {
        cmInAppManager.onDialogShown(activity)
    }

    override fun onInAppDataConsumed(cmInApp: CMInApp) {
        cmInAppManager.dataConsumed(cmInApp)
    }

    override fun onCMInAppInflateException(cmInApp: CMInApp) {
        cmInAppManager.onCMInAppInflateException(cmInApp)
    }

    override fun onUserInteractedWithInAppView(cmInApp: CMInApp) {
        cmInAppManager.onCMinAppInteraction(cmInApp)
    }

    override fun onUserInteractedWithInAppView(cmInAppId: Long) {
        cmInAppManager.onCMinAppInteraction(cmInAppId)
    }

    override fun onInAppViewDismiss(cmInApp: CMInApp) {
        cmInAppManager.onCMinAppDismiss(cmInApp)
    }


    override fun onInAppViewDismiss(activity: Activity) {
        cmInAppManager.onDialogDismiss(activity)
    }

    override fun isInAppViewVisible(activity: Activity): Boolean {
        return cmInAppManager.isDialogVisible(activity)
    }
}