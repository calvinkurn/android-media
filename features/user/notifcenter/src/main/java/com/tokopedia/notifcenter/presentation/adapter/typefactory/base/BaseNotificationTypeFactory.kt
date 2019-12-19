package com.tokopedia.notifcenter.presentation.adapter.typefactory.base

import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationItemViewBean

interface BaseNotificationTypeFactory {
    fun type(viewItem: NotificationItemViewBean): Int
}