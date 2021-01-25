package com.tokopedia.moengage_wrapper.interfaces

import android.net.Uri
import android.os.Bundle
import com.moengage.pushbase.push.MoEPushCallBacks

class MoengagePushListenerImpl(val listener: MoengagePushListener): MoEPushCallBacks.OnMoEPushNavigationAction {
    override fun onClick(screenName: String?, extras: Bundle?, deepLinkUri: Uri?): Boolean {
        return listener.onClick(screenName, extras, deepLinkUri)
    }

}

interface MoengagePushListener {
    fun onClick(screenName: String?, extras: Bundle?, deepLinkUri: Uri?): Boolean
}