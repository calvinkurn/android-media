package com.tokopedia.notifcenter.presentation.view.listener

import com.tokopedia.notifcenter.analytics.NotificationUpdateAnalytics
import com.tokopedia.notifcenter.domain.pojo.ProductData

import com.tokopedia.notifcenter.presentation.view.viewmodel.NotificationUpdateItemViewModel

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