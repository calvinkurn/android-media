package com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.feature.ordertracking.domain.constants.OrderStatusType
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderDetailUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderStatusUseCase
import com.tokopedia.tokofood.feature.ordertracking.presentation.adapter.BaseOrderTrackingTypeFactory
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailResultUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderStatusLiveTrackingUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import dagger.Lazy
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TokoFoodOrderTrackingViewModel @Inject constructor(
    private val coroutineDispatchers: CoroutineDispatchers,
    private val getTokoFoodOrderDetailUseCase: Lazy<GetTokoFoodOrderDetailUseCase>,
    private val getTokoFoodOrderStatusUseCase: Lazy<GetTokoFoodOrderStatusUseCase>
) : BaseViewModel(coroutineDispatchers.main) {

    private val _orderDetailResult = MutableLiveData<Result<OrderDetailResultUiModel>>()
    val orderDetailResult: LiveData<Result<OrderDetailResultUiModel>>
        get() = _orderDetailResult

    private val _orderLiveTrackingStatus =
        MutableSharedFlow<Result<OrderStatusLiveTrackingUiModel>>(replay = Int.ONE)

    val orderLiveTrackingStatus: SharedFlow<Result<OrderStatusLiveTrackingUiModel>> = _orderLiveTrackingStatus

    private val _orderId = MutableStateFlow("")

    private val _orderCompletedLiveTracking = MutableLiveData<Result<OrderDetailResultUiModel>>()
    val orderCompletedLiveTracking: LiveData<Result<OrderDetailResultUiModel>>
        get() = _orderCompletedLiveTracking

    private val canceledOrder = R.raw.orderdetailcancelled
    private val successOrder = R.raw.orderdetailsuccess

    private val resources = listOf(
        R.raw.order_tracking_created,
        R.raw.order_tracking_new,
        R.raw.order_tracking_awaiting_merchant_acceptance,
        R.raw.order_tracking_merchant_accepted,
        R.raw.order_tracking_searching_driver,
        R.raw.order_tracking_otw_pickup,
        R.raw.order_tracking_driver_arrived,
        R.raw.order_tracking_pickup_requested,
        R.raw.order_tracking_order_placed,
        R.raw.order_tracking_otw_destination,
        R.raw.orderdetailsuccess
    )

    private var foodItems = listOf<BaseOrderTrackingTypeFactory>()
    private var orderId = ""

    init {
        fetchOrderLiveTracking()
    }

    fun fetchOrderDetail(resourceId: Int) {
        launchCatchError(block = {
            val orderDetailResult = withContext(coroutineDispatchers.io) {
                getTokoFoodOrderDetailUseCase.get().executeTemp(resourceId)
            }
            this@TokoFoodOrderTrackingViewModel.foodItems = orderDetailResult.foodItemList
            _orderDetailResult.value = Success(orderDetailResult)
        }, onError = {
            _orderDetailResult.value = Fail(it)
        })
    }

    private fun fetchOrderCompletedLiveTracking(resourceId: Int) {
        launchCatchError(block = {
            val orderDetailResult = withContext(coroutineDispatchers.io) {
                getTokoFoodOrderDetailUseCase.get().executeTemp(resourceId)
            }
            this@TokoFoodOrderTrackingViewModel.foodItems = orderDetailResult.foodItemList
            _orderCompletedLiveTracking.value = Success(orderDetailResult)
        }, onError = {
            _orderCompletedLiveTracking.value = Fail(it)
        })
    }

    fun getFoodItems() = foodItems
    fun getOrderId() = orderId

    fun updateOrderId(orderId: String) {
        _orderId.tryEmit(orderId)
        this.orderId = orderId
    }

    private fun fetchOrderLiveTracking() {
        viewModelScope.launchCatchError(block = {
            var index = 0
            _orderId.collect {
                delay(DELAY_ORDER_STATE)
                val orderStatusResult = withContext(coroutineDispatchers.io) {
                    getTokoFoodOrderStatusUseCase.get().executeTemp(resources[index])
                }
                index++
                when (orderStatusResult.orderStatusKey) {
                    OrderStatusType.CANCELLED -> {
                        fetchOrderCompletedLiveTracking(canceledOrder)
                    }
                    OrderStatusType.COMPLETED -> {
                        fetchOrderCompletedLiveTracking(successOrder)
                    }
                    else -> {
                        _orderLiveTrackingStatus.emit(Success(orderStatusResult))
                    }
                }
            }
        }, onError = {
            _orderLiveTrackingStatus.emit(Fail(it))
        })
    }

    companion object {
        const val DELAY_ORDER_STATE = 5000L
        const val STOP_TIMEOUT_SUBSCRIBED = 5000L
    }
}