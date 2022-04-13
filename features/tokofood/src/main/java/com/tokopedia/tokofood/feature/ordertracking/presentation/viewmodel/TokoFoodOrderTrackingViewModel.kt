package com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderDetailUseCase
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailResultUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokoFoodOrderTrackingViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val getTokoFoodOrderDetailUseCase: Lazy<GetTokoFoodOrderDetailUseCase>
): BaseViewModel(coroutineDispatchers.main) {

    private val _orderDetailResult = MutableLiveData<Result<OrderDetailResultUiModel>>()
    val orderDetailResult: LiveData<Result<OrderDetailResultUiModel>>
        get() = _orderDetailResult


    private var foodItems = listOf<BaseOrderTrackingTypeFactory>()

    init {

    }

    fun fetchOrderDetail(json: String) {
        launchCatchError(block = {
            val orderDetailResult = withContext(coroutineDispatchers.io) {
                getTokoFoodOrderDetailUseCase.get().executeTemp(json)
            }
            foodItems = orderDetailResult.foodItemList
            _orderDetailResult.value = Success(orderDetailResult)
        }, onError = {
            _orderDetailResult.value = Fail(it)
        })
    }

    fun getFoodItems() = foodItems

}