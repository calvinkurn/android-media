package com.tokopedia.navigation.presentation.view.listener

import com.tokopedia.navigation.analytics.NotificationUpdateAnalytics
import com.tokopedia.navigation.domain.pojo.ProductData

import com.tokopedia.navigation.presentation.view.viewmodel.NotificationUpdateItemViewModel

/**
 * @author : Steven 12/04/19
 */
interface NotificationUpdateItemListener {

    fun itemClicked(viewModel: NotificationUpdateItemViewModel, adapterPosition: Int)

    fun getAnalytic(): NotificationUpdateAnalytics

    fun addProductToCart(product: ProductData, onSuccessAddToCart: () -> Unit)

    fun showTextLonger(element: NotificationUpdateItemViewModel)

    fun trackNotificationImpression(element: NotificationUpdateItemViewModel)
}