package com.tokopedia.sellerappwidget.view.viewmodel

import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.sellerappwidget.coroutine.AppWidgetDispatcherProvider
import com.tokopedia.sellerappwidget.di.AppWidgetScope
import com.tokopedia.sellerappwidget.domain.usecase.GetOrderUseCase
import com.tokopedia.sellerappwidget.view.viewmodel.view.OrderAppWidgetView
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Created By @ilhamsuaib on 16/11/20
 */

@AppWidgetScope
class OrderAppWidgetViewModel @Inject constructor(
        private val getOrderUseCase: Lazy<GetOrderUseCase>,
        private val dispatcherProvider: AppWidgetDispatcherProvider
) : BaseAppWidgetVM<OrderAppWidgetView>(dispatcherProvider) {

    fun getOrderList(dateFrm: String) {
        launchCatchError(block = {
            getOrderUseCase.get().params = GetOrderUseCase.createParams(dateFrm)
            val result = Success(withContext(dispatcherProvider.io) {
                getOrderUseCase.get().executeOnBackground()
            })
            delay(2000L)
            view?.onSuccessGetOrderList(result)
        }, onError = {
            view?.onFailedGetOrderList(Fail(it))
        })
    }
}