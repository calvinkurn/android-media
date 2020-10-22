package com.tokopedia.notifications.inApp

import com.tokopedia.notifications.inApp.ruleEngine.storage.entities.inappdata.CMInApp

interface InAppPopupContract {
    fun handleInAppPopup(data: CMInApp, entityHashCode: Int)
}