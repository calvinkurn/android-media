package com.tokopedia.notifcenter.data.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationTypeFactory

class EmptyNotificationUiModel constructor(
    val hasFilter: Boolean,
    val title: String? = null,
    val description: String? = null
) : Visitable<NotificationTypeFactory> {

    override fun type(typeFactory: NotificationTypeFactory): Int {
        return typeFactory.type(this)
    }
}
