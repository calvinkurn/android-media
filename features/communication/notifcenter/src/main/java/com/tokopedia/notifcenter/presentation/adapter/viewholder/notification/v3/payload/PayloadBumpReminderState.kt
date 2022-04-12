package com.tokopedia.notifcenter.presentation.adapter.viewholder.notification.v3.payload

import com.tokopedia.notifcenter.data.entity.notification.ProductData
import com.tokopedia.notifcenter.data.uimodel.NotificationUiModel

class PayloadBumpReminderState(
        val productData: ProductData,
        val notification: NotificationUiModel
)