package com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain

import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData
import rx.Observable
import java.util.*

/**
 * Created by kris on 11/17/17. Tokopedia
 */
interface IOrderHistoryRepository {
    fun requestOrderHistoryData(params: HashMap<String?, Any?>?): OrderHistoryData
}