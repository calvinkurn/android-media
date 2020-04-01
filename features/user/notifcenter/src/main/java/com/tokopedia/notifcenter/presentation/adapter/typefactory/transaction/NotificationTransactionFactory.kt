package com.tokopedia.notifcenter.presentation.adapter.typefactory.transaction

import com.tokopedia.notifcenter.data.viewbean.NotificationFilterSectionViewBean
import com.tokopedia.notifcenter.data.viewbean.PurchaseNotificationViewBean
import com.tokopedia.notifcenter.data.viewbean.SaleNotificationViewBean
import com.tokopedia.notifcenter.presentation.adapter.typefactory.base.BaseNotificationTypeFactory

interface NotificationTransactionFactory : BaseNotificationTypeFactory {
    fun type(purchaseNotification: PurchaseNotificationViewBean): Int
    fun type(saleNotification: SaleNotificationViewBean): Int
    fun type(filter: NotificationFilterSectionViewBean): Int
}