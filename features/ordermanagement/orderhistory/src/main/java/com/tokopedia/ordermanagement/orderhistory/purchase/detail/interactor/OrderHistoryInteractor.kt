package com.tokopedia.ordermanagement.orderhistory.purchase.detail.interactor

import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData
import rx.Subscriber
import java.util.*

/**
 * Created by kris on 11/17/17. Tokopedia
 */
interface OrderHistoryInteractor {
    fun requestOrderHistoryData(subscriber: Subscriber<OrderHistoryData>,
                                params: HashMap<String?, Any?>?)

    fun onViewDestroyed()
}