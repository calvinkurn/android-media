package com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain.usecase

import com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain.OrderHistoryRepository
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData
import com.tokopedia.usecase.coroutines.UseCase
import java.util.*
import javax.inject.Inject

class OrderHistoryUseCase @Inject constructor(private val orderHistoryRepository: OrderHistoryRepository) : UseCase<OrderHistoryData>() {

    private var requestParams: HashMap<String?, Any?>? = null

    override suspend fun executeOnBackground(): OrderHistoryData {
        return orderHistoryRepository.requestOrderHistoryData(requestParams)
    }

    fun setRequestParams(requestParams: HashMap<String?, Any?>?) {
        this.requestParams = requestParams
    }

}