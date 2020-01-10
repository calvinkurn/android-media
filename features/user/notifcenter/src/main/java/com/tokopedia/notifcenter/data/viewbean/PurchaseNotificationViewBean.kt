package com.tokopedia.notifcenter.data.viewbean

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.data.model.DrawerNotification
import com.tokopedia.notifcenter.presentation.adapter.typefactory.transaction.NotificationTransactionFactory

class PurchaseNotificationViewBean: DrawerNotification(), Visitable<NotificationTransactionFactory> {

    override fun type(typeFactory: NotificationTransactionFactory): Int {
        return typeFactory.type(this)
    }

}