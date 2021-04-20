package com.tokopedia.notifications.inApp

import com.tokopedia.notifications.common.IrisAnalyticsEvents
import com.tokopedia.notifications.inApp.ruleEngine.RulesManager
import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp

class CmDataConsumer(private val sendPushContract: SendPushContract) {

    fun dataConsumed(inAppData: CMInApp) {
        RulesManager.getInstance().dataConsumed(inAppData.id)
        sendPushContract.sendPushEvent(inAppData, IrisAnalyticsEvents.INAPP_RECEIVED, null)
    }
}