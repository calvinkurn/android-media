package com.tokopedia.moengage_wrapper.interfaces

import android.content.Context
import android.net.Uri
import android.os.Bundle
import com.moengage.inapp.InAppManager
import com.moengage.inapp.InAppMessage
import com.moengage.inapp.InAppTracker
import com.tokopedia.moengage_wrapper.constants.Constants

class MoengageInAppListenerImpl(val listener: MoengageInAppListener, val context: Context) : InAppManager.InAppMessageListener {
    override fun onInAppShown(message: InAppMessage?) {
        if (Constants.PackageName.SELLERAPP_PACKAGE == context.packageName)
            InAppTracker.getInstance(context).trackInAppClicked(message)
    }

    override fun onInAppClick(screenName: String?, extras: Bundle?, deepLinkUri: Uri?): Boolean {
        return listener.onInAppClick(screenName, extras, deepLinkUri)
    }

    override fun showInAppMessage(message: InAppMessage?): Boolean {
        InAppTracker.getInstance(context).trackInAppClicked(message)
        return true
    }

    override fun onInAppClosed(message: InAppMessage?) {
    }
}

interface MoengageInAppListener {
    fun onInAppClick(screenName: String?, extras: Bundle?, deepLinkUri: Uri?): Boolean
}