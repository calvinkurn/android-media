package com.tokopedia.ordermanagement.orderhistory.purchase.detail.activity

import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData

/**
 * Created by kris on 11/17/17. Tokopedia
 */
interface OrderHistoryView {
    fun receivedHistoryData(data: OrderHistoryData)
    fun onLoadError(message: String?)
    fun showMainViewLoadingPage()
    fun hideMainViewLoadingPage()
}