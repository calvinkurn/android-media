package com.tokopedia.sellerappwidget.view.viewmodel

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerappwidget.di.AppWidgetScope
import com.tokopedia.sellerappwidget.domain.usecase.GetOrderUseCase
import com.tokopedia.sellerappwidget.view.model.OrderItemUiModel
import com.tokopedia.sellerappwidget.view.model.OrderUiModel
import com.tokopedia.sellerappwidget.view.viewmodel.view.AppWidgetView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 16/11/20
 */

@AppWidgetScope
class OrderAppWidgetViewModel @Inject constructor(
        private val getOrderUseCase: Lazy<GetOrderUseCase>,
        private val dispatchers: CoroutineDispatchers
) : BaseAppWidgetVM<AppWidgetView<OrderUiModel>>(dispatchers) {

    fun getOrderList(startDateFrm: String, endDateFrm: String) {
        launchCatchError(block = {
            getOrderUseCase.get().params = GetOrderUseCase.createParams(startDateFrm, endDateFrm)
            val result = Success(withContext(dispatchers.io) {
                getOrderUseCase.get().executeOnBackground()
            })
            view?.onSuccessGetOrderList(result)
        }, onError = {
            view?.onFailedGetOrderList(Fail(it))
        })
    }
}