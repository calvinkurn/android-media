package com.tokopedia.notifcenter.presentation.adapter.typefactory.base

import com.tokopedia.notifcenter.data.viewbean.NotificationEmptyStateViewBean
import com.tokopedia.notifcenter.data.viewbean.NotificationItemViewBean

interface BaseNotificationTypeFactory {
    fun type(viewItem: NotificationItemViewBean): Int
    fun type(emptyState: NotificationEmptyStateViewBean): Int
}