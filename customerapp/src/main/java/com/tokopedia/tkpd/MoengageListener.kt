package com.tokopedia.tkpd

import android.net.Uri
import android.os.Bundle
import com.moengage.inapp.InAppManager
import com.moengage.inapp.InAppMessage
import com.moengage.inapp.InAppTracker
import com.moengage.pushbase.push.MoEPushCallBacks
import com.tokopedia.promotionstarget.presentation.GratifCmInitializer

class MoengageListener(private val notificationClickHandler: NotificationClickHandler,
                       private val inAppTracker: InAppTracker) : InAppManager.InAppMessageListener,
        MoEPushCallBacks.OnMoEPushNavigationAction {

    private val callbacks = ArrayList<Function2<Int, Boolean, Int>?>()

    init {
        callbacks.add(GratifCmInitializer.getMoengageListener())
    }

    override fun onInAppShown(message: InAppMessage?) {
        if (message != null) {
            callbacks.forEach {
                it?.invoke(message.theComposedView?.context?.hashCode() ?: 0, true)
            }
        }
    }

    override fun onInAppClick(screenName: String?, extras: Bundle?, deepLinkUri: Uri?): Boolean {
        return notificationClickHandler.handleClick(screenName, extras, deepLinkUri)
    }

    override fun showInAppMessage(message: InAppMessage?): Boolean {
//        InAppTracker.getInstance(this).trackInAppClicked(message);
        inAppTracker.trackInAppClicked(message);
        return true;
    }

    override fun onInAppClosed(message: InAppMessage?) {
        if (message != null) {
            callbacks.forEach {
                it?.invoke(message.theComposedView?.context?.hashCode() ?: 0, false)
            }
        }
    }

    override fun onClick(screenName: String?, extras: Bundle?, deepLinkUri: Uri?): Boolean {
        return notificationClickHandler.handleClick(screenName, extras, deepLinkUri)
    }
}

interface NotificationClickHandler {
    fun handleClick(screenName: String?, extras: Bundle?, deepLinkUri: Uri?): Boolean
}