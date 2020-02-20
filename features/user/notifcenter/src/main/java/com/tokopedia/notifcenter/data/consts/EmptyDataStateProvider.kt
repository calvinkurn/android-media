package com.tokopedia.notifcenter.data.consts

import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.state.EmptySource
import com.tokopedia.notifcenter.data.viewbean.NotificationEmptyStateViewBean

object EmptyDataStateProvider {

    fun emptyData(source: EmptySource = EmptySource.Normal): NotificationEmptyStateViewBean {
        return NotificationEmptyStateViewBean(
                R.drawable.bg_notif_empty_state,
                R.string.notification_empty_message,
                source)
    }

    fun clearEmptyData(): NotificationEmptyStateViewBean {
        return NotificationEmptyStateViewBean()
    }

}