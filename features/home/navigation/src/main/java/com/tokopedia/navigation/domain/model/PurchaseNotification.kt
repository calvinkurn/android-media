package com.tokopedia.navigation.domain.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.navigation.presentation.adapter.typefactory.NotificationTransactionFactory

class PurchaseNotification: DrawerNotification(), Visitable<NotificationTransactionFactory> {

    override fun type(typeFactory: NotificationTransactionFactory): Int {
        return typeFactory.type(this)
    }

}