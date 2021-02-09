package com.tokopedia.notifcenter.data.entity.notification

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory

class NotificationDetailResponseModel constructor(
        val items: List<Visitable<NotificationTypeFactory>>,
        val hasNext: Boolean
)