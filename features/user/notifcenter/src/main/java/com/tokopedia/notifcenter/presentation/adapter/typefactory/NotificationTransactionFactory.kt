package com.tokopedia.notifcenter.presentation.adapter.typefactory

import com.tokopedia.notifcenter.domain.model.NotificationFilterSectionWrapper
import com.tokopedia.notifcenter.domain.model.PurchaseNotification
import com.tokopedia.notifcenter.domain.model.SaleNotification
import com.tokopedia.notifcenter.domain.model.TransactionItemNotification
import com.tokopedia.notifcenter.domain.model.EmptyState

interface NotificationTransactionFactory {
    fun type(purchaseNotification: PurchaseNotification): Int
    fun type(saleNotification: SaleNotification): Int
    fun type(notification: TransactionItemNotification): Int
    fun type(filter: NotificationFilterSectionWrapper): Int
    fun type(empty:  EmptyState): Int
}