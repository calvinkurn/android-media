package com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain

import com.google.gson.Gson
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain.mapper.OrderDetailMapper
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.response.OrderHistoryResponse
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData
import rx.Observable
import java.util.*

/**
 * Created by kris on 11/17/17. Tokopedia
 */
class OrderHistoryRepository(private val service: OrderDetailService, private val mapper: OrderDetailMapper) : IOrderHistoryRepository {
    override fun requestOrderHistoryData(params: HashMap<String?, Any?>?): Observable<OrderHistoryData?> {
        return service.api.getOrderHistory(params).map { stringResponse ->
            mapper.getOrderHistoryData(
                    Gson().fromJson(stringResponse.body(), OrderHistoryResponse::class.java))
        }
    }

}