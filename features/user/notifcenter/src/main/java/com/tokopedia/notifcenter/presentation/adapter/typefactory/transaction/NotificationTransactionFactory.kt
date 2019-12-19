package com.tokopedia.notifcenter.presentation.adapter.typefactory.transaction

import com.tokopedia.notifcenter.domain.model.NotificationFilterSectionWrapper
import com.tokopedia.notifcenter.domain.model.PurchaseNotification
import com.tokopedia.notifcenter.domain.model.SaleNotification
import com.tokopedia.notifcenter.domain.model.EmptyState
import com.tokopedia.notifcenter.presentation.adapter.typefactory.base.BaseNotificationTypeFactory

interface NotificationTransactionFactory : BaseNotificationTypeFactory {
    fun type(purchaseNotification: PurchaseNotification): Int
    fun type(saleNotification: SaleNotification): Int
    fun type(filter: NotificationFilterSectionWrapper): Int
    fun type(empty:  EmptyState): Int
}