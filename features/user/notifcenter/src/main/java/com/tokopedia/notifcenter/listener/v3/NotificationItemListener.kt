package com.tokopedia.notifcenter.listener.v3

import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel

interface NotificationItemListener {
    fun showLongerContent(element: NotificationUiModel)
}