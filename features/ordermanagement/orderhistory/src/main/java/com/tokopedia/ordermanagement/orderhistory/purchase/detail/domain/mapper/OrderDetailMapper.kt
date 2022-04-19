package com.tokopedia.ordermanagement.orderhistory.purchase.detail.domain.mapper

import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.response.OrderHistoryResponse
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryData
import com.tokopedia.ordermanagement.orderhistory.purchase.detail.model.history.viewmodel.OrderHistoryListData

/**
 * Created by kris on 11/23/17. Tokopedia
 */
class OrderDetailMapper {

    fun getOrderHistoryData(response: OrderHistoryResponse): OrderHistoryData {
        val viewData = OrderHistoryData()
        val historyData = response.data
        viewData.stepperMode = historyData.orderStatusCode
        viewData.stepperStatusTitle = historyData.historyTitle
        viewData.historyImage = historyData.historyImg
        viewData.orderListData = historyData.histories.map { OrderHistoryListData(it.status, it.hour, it.date, it.comment, it.actionBy, it.orderStatusColor) }
        return viewData
    }
}