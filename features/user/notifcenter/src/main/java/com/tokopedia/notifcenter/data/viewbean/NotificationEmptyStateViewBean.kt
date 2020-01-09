package com.tokopedia.notifcenter.data.viewbean

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.presentation.adapter.typefactory.base.BaseNotificationTypeFactory

open class NotificationEmptyStateViewBean(
        val icon: Int = 0,
        val title: Int = 0
) : Visitable<BaseNotificationTypeFactory> {

    override fun type(typeFactory: BaseNotificationTypeFactory): Int {
        return typeFactory.type(this)
    }

}