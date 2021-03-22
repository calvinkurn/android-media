package com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain

import com.google.gson.Gson
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain.mapper.OrderDetailMapper
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.response.OrderHistoryResponse
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData
import java.util.*

/**
 * Created by kris on 11/17/17. Tokopedia
 */
class OrderHistoryRepository(private val service: OrderDetailService, private val mapper: OrderDetailMapper) : IOrderHistoryRepository {

    override fun requestOrderHistoryData(params: HashMap<String?, Any?>?): OrderHistoryData {
        val response = service.api.getOrderHistory(params).execute().body()
        if (!response.isNullOrEmpty()) {
            return mapper.getOrderHistoryData(Gson().fromJson(response, OrderHistoryResponse::class.java))
        }
        throw MessageErrorException("")
    }

}