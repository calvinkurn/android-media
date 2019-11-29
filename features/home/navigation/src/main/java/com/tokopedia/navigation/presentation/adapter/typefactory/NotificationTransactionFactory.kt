package com.tokopedia.navigation.presentation.adapter.typefactory

import com.tokopedia.navigation.domain.model.NotificationFilterSectionWrapper
import com.tokopedia.navigation.domain.model.PurchaseNotification
import com.tokopedia.navigation.domain.model.SaleNotification
import com.tokopedia.navigation.domain.model.TransactionItemNotification
import com.tokopedia.navigation.domain.model.EmptyState

interface NotificationTransactionFactory {
    fun type(purchaseNotification: PurchaseNotification): Int
    fun type(saleNotification: SaleNotification): Int
    fun type(notification: TransactionItemNotification): Int
    fun type(filter: NotificationFilterSectionWrapper): Int
    fun type(empty:  EmptyState): Int
}