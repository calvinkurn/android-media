package com.tokopedia.buyerorder.unifiedhistory.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.buyerorder.common.BuyerDispatcherProvider
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListParam
import com.tokopedia.buyerorder.unifiedhistory.list.data.model.UohListOrder
import com.tokopedia.buyerorder.unifiedhistory.list.domain.UohListUseCase
import com.tokopedia.usecase.coroutines.Result
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by fwidjaja on 03/07/20.
 */
class UohListViewModel @Inject constructor(dispatcher: BuyerDispatcherProvider,
                                           private val uohListUseCase: UohListUseCase) : BaseViewModel(dispatcher.ui()) {

    private val _orderHistoryListResult = MutableLiveData<Result<UohListOrder.Data.UohOrders>>()
    val orderHistoryListResult: LiveData<Result<UohListOrder.Data.UohOrders>>
        get() = _orderHistoryListResult

    fun loadOrderList(orderQuery: String, paramOrder: UohListParam) {
        launch {
            _orderHistoryListResult.postValue(uohListUseCase.execute(paramOrder, orderQuery))
        }
    }
}