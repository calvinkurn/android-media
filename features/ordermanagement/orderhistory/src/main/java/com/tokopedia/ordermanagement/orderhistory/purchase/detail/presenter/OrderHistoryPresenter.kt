package com.tokopedia.ordermanagement.orderhistory.purchase.detail.presenter

import android.content.Context

/**
 * Created by kris on 11/17/17. Tokopedia
 */
interface OrderHistoryPresenter {
    fun fetchHistoryData(context: Context, orderId: String, userMode: Int)
    fun onDestroy()
}