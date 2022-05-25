package com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokofood.feature.ordertracking.domain.constants.OrderStatusType
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetDriverPhoneNumberUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderDetailUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderStatusUseCase
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverPhoneNumberUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel
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
    private val getTokoFoodOrderStatusUseCase: Lazy<GetTokoFoodOrderStatusUseCase>,
    private val getDriverPhoneNumberUseCase: Lazy<GetDriverPhoneNumberUseCase>
) : BaseViewModel(coroutineDispatchers.main) {

    private val _orderDetailResult = MutableLiveData<Result<OrderDetailResultUiModel>>()
    val orderDetailResult: LiveData<Result<OrderDetailResultUiModel>>
        get() = _orderDetailResult

    private val _orderLiveTrackingStatus =
        MutableSharedFlow<Result<OrderStatusLiveTrackingUiModel>>(replay = Int.ONE)

    val orderLiveTrackingStatus: SharedFlow<Result<OrderStatusLiveTrackingUiModel>> =
        _orderLiveTrackingStatus

    private val _orderId = MutableStateFlow("")

    private val _orderCompletedLiveTracking = MutableLiveData<Result<OrderDetailResultUiModel>>()
    val orderCompletedLiveTracking: LiveData<Result<OrderDetailResultUiModel>>
        get() = _orderCompletedLiveTracking

    private val _driverPhoneNumber = MutableLiveData<Result<DriverPhoneNumberUiModel>>()
    val driverPhoneNumber: LiveData<Result<DriverPhoneNumberUiModel>>
        get() = _driverPhoneNumber

    private var foodItems = listOf<FoodItemUiModel>()
    private var orderId = ""
    private var orderStatus = ""

    init {
        fetchOrderLiveTracking()
    }

    fun getFoodItems() = foodItems
    fun getOrderId() = orderId

    fun updateOrderId(orderId: String) {
        _orderId.tryEmit(orderId)
        this.orderId = orderId
    }

    fun fetchOrderDetail(orderId: String) {
        launchCatchError(block = {
            val orderDetailResult = withContext(coroutineDispatchers.io) {
                getTokoFoodOrderDetailUseCase.get().execute(orderId)
            }
            this@TokoFoodOrderTrackingViewModel.orderStatus = orderDetailResult.orderStatus
            this@TokoFoodOrderTrackingViewModel.foodItems =
                orderDetailResult.foodItemList.filterIsInstance<FoodItemUiModel>()
            _orderDetailResult.value = Success(orderDetailResult)
        }, onError = {
            _orderDetailResult.value = Fail(it)
        })
    }

    fun fetchDriverPhoneNumber(orderId: String) {
        launchCatchError(block = {
            val driverPhoneNumberResult = withContext(coroutineDispatchers.io) {
                getDriverPhoneNumberUseCase.get().execute(orderId)
            }
            _driverPhoneNumber.value = Success(driverPhoneNumberResult)
        }, onError = {
            _driverPhoneNumber.value = Fail(it)
        })
    }

    private fun fetchOrderCompletedLiveTracking(orderId: String) {
        launchCatchError(block = {
            val orderDetailResult = withContext(coroutineDispatchers.io) {
                getTokoFoodOrderDetailUseCase.get().execute(orderId)
            }
            this@TokoFoodOrderTrackingViewModel.orderStatus = orderDetailResult.orderStatus
            this@TokoFoodOrderTrackingViewModel.foodItems =
                orderDetailResult.foodItemList.filterIsInstance(FoodItemUiModel::class.java)
            _orderCompletedLiveTracking.value = Success(orderDetailResult)
        }, onError = {
            _orderCompletedLiveTracking.value = Fail(it)
        })
    }

    private fun fetchOrderLiveTracking() {
        viewModelScope.launchCatchError(block = {
            _orderId.collect {
                delay(DELAY_ORDER_STATE)
                val orderStatusResult = withContext(coroutineDispatchers.io) {
                    getTokoFoodOrderStatusUseCase.get().execute(it)
                }
                this@TokoFoodOrderTrackingViewModel.orderStatus = orderStatusResult.orderStatusKey
                when (orderStatus) {
                    OrderStatusType.CANCELLED, OrderStatusType.COMPLETED -> {
                        fetchOrderCompletedLiveTracking(it)
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

        const val ORDER_ID_CANCELLED_DUMMY = "422acc7a-ef9a-4486-8a60-2c4f3c48f12e"
    }
}