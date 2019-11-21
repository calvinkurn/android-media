package com.tokopedia.navigation.presentation.view.listener

import com.tokopedia.navigation.analytics.NotificationUpdateAnalytics
import com.tokopedia.navigation.domain.model.TransactionItemNotification
import com.tokopedia.navigation.domain.pojo.ProductData

interface NotificationTransactionItemListener {
    fun itemClicked(viewModel: TransactionItemNotification, adapterPosition: Int)

    fun getAnalytic(): NotificationUpdateAnalytics

    fun addProductToCart(product: ProductData, onSuccessAddToCart: () -> Unit)

    fun showTextLonger(element: TransactionItemNotification)

    fun trackNotificationImpression(element: TransactionItemNotification)
}