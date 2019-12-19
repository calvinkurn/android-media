package com.tokopedia.notifcenter.domain.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.presentation.adapter.typefactory.transaction.NotificationTransactionFactory

class EmptyState(
        val icon: Int,
        val title: String
): Visitable<NotificationTransactionFactory> {

    override fun type(typeFactory: NotificationTransactionFactory): Int {
        return typeFactory.type(this)
    }

}