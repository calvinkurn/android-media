package com.tokopedia.navigation.presentation.adapter.typefactory

import com.tokopedia.navigation.domain.model.PurchaseNotification
import com.tokopedia.navigation.domain.model.SaleNotification
import com.tokopedia.navigation.domain.model.TransactionItemNotification

interface NotificationTransactionFactory {
    fun type(purchaseNotification: PurchaseNotification): Int
    fun type(saleNotification: SaleNotification): Int
    fun type(notification: TransactionItemNotification): Int
}