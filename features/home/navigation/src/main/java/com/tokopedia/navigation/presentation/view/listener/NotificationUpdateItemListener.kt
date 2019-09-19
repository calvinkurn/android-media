package com.tokopedia.navigation.presentation.view.listener

import com.tokopedia.navigation.analytics.NotificationUpdateAnalytics
import com.tokopedia.navigation.domain.pojo.ProductData

/**
 * @author : Steven 12/04/19
 */
interface NotificationUpdateItemListener {

    fun itemClicked(notifId: String, adapterPosition: Int, needToResetCounter: Boolean, templateKey: String)

    fun getAnalytic(): NotificationUpdateAnalytics

    fun addProductToCart(product: ProductData, onSuccessAddToCart: () -> Unit)
}