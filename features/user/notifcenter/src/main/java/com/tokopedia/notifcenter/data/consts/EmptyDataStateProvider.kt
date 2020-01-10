package com.tokopedia.notifcenter.data.consts

import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.viewbean.NotificationEmptyStateViewBean

object EmptyDataStateProvider {

    fun emptyData(): NotificationEmptyStateViewBean {
        return NotificationEmptyStateViewBean(
                R.drawable.notifcenter_bg_empty_state,
                R.string.notification_empty_message)
    }

    fun clearEmptyData(): NotificationEmptyStateViewBean {
        return NotificationEmptyStateViewBean()
    }

}