package com.tokopedia.product.manage.oldlist.utils

import com.tokopedia.product.manage.oldlist.analytics.EventTracking
import com.tokopedia.product.manage.oldlist.constant.*
import com.tokopedia.track.TrackApp

object ProductManageTracking {

    private fun eventProductManage(action: String, label: String) {
        TrackApp.getInstance().gtm.sendGeneralEvent(EventTracking(
                EVENT_MANAGE_PRODUCT,
                MANAGE_PRODUCT,
                action,
                label
        ).event)
    }

    fun eventDraftClick(label: String) {
        eventProductManage(CLICK, label)
    }
}