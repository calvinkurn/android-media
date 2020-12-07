package com.tokopedia.notifications.inApp

import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp

interface SendPushContract {
    fun sendPushEvent(cmInApp: CMInApp?, eventName: String?, elementId: String?)
}