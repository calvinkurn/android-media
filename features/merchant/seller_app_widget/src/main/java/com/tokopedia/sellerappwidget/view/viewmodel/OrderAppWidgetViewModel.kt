package com.tokopedia.sellerappwidget.view.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerappwidget.common.Const
import com.tokopedia.sellerappwidget.domain.usecase.GetOrderUseCase
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.sellerappwidget.view.viewmodel.view.AppWidgetView
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

/**
 * Created By @ilhamsuaib on 16/11/20
 */

class OrderAppWidgetViewModel(
        private val getNewOrderUseCase: GetOrderUseCase,
        private val getReadyToShipOrderUseCase: GetOrderUseCase,
        private val dispatchers: CoroutineDispatchers
) : BaseAppWidgetVM<AppWidgetView<OrderUiModel>>(dispatchers) {

    fun getOrderList(startDateFrm: String, endDateFrm: String) {
        launchCatchError(block = {
            getNewOrderUseCase.params = GetOrderUseCase.createParams(startDateFrm, endDateFrm,
                    Const.OrderStatusId.NEW_ORDER, Const.OrderListSortBy.SORT_BY_PAYMENT_DATE_DESCENDING)
            getReadyToShipOrderUseCase.params = GetOrderUseCase.createParams(startDateFrm, endDateFrm,
                    Const.OrderStatusId.READY_TO_SHIP, Const.OrderListSortBy.SORT_BY_PAYMENT_DATE_ASCENDING)
            val result = withContext(dispatchers.io) {
                val getNewOrderList = async { getNewOrderUseCase.executeOnBackground() }
                val getReadyToShipList = async { getReadyToShipOrderUseCase.executeOnBackground() }
                val newOrderResult = getNewOrderList.await()
                val readyToShipResult = getReadyToShipList.await()
                return@withContext OrderUiModel(
                        orders = newOrderResult.orders.orEmpty().plus(readyToShipResult.orders.orEmpty()),
                        sellerOrderStatus = readyToShipResult.sellerOrderStatus
                )
            }
            view?.onSuccess(result)
        }, onError = {
            view?.onError(it)
        })
    }
}