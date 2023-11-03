package com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokochat.common.util.TokoChatValueUtil
import com.tokopedia.tokochat.config.domain.TokoChatCounterUseCase
import com.tokopedia.tokochat.config.domain.TokoChatGroupBookingUseCase
import com.tokopedia.tokochat.config.util.TokoChatResult
import com.tokopedia.tokochat.config.util.TokoChatServiceType
import com.tokopedia.tokofood.feature.ordertracking.domain.constants.OrderStatusType
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetDriverPhoneNumberUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderDetailUseCase
import com.tokopedia.tokofood.feature.ordertracking.domain.usecase.GetTokoFoodOrderStatusUseCase
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.DriverPhoneNumberUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.FoodItemUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.MerchantDataUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderDetailResultUiModel
import com.tokopedia.tokofood.feature.ordertracking.presentation.uimodel.OrderStatusLiveTrackingUiModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
open class TokoFoodOrderTrackingViewModel @Inject constructor(
    val userSession: UserSessionInterface,
    private val savedStateHandle: SavedStateHandle,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val getTokoFoodOrderDetailUseCase: Lazy<GetTokoFoodOrderDetailUseCase>,
    private val getTokoFoodOrderStatusUseCase: Lazy<GetTokoFoodOrderStatusUseCase>,
    private val getDriverPhoneNumberUseCase: Lazy<GetDriverPhoneNumberUseCase>,
    private val getTokoChatGroupBookingUseCase: Lazy<TokoChatGroupBookingUseCase>,
    private val getTokoChatCounterUseCase: Lazy<TokoChatCounterUseCase>
) : BaseViewModel(coroutineDispatchers.main), DefaultLifecycleObserver {

    private val _orderDetailResult = MutableLiveData<Result<OrderDetailResultUiModel>>()
    val orderDetailResult: LiveData<Result<OrderDetailResultUiModel>>
        get() = _orderDetailResult

    private val _orderLiveTrackingStatus =
        MutableSharedFlow<Result<OrderStatusLiveTrackingUiModel>>(replay = Int.ONE)

    val orderLiveTrackingStatus: SharedFlow<Result<OrderStatusLiveTrackingUiModel>> =
        _orderLiveTrackingStatus

    val orderIdFlow = MutableSharedFlow<String>(Int.ONE)

    private val _orderCompletedLiveTracking = MutableLiveData<Result<OrderDetailResultUiModel>>()
    val orderCompletedLiveTracking: LiveData<Result<OrderDetailResultUiModel>>
        get() = _orderCompletedLiveTracking

    private val _driverPhoneNumber = MutableLiveData<Result<DriverPhoneNumberUiModel>>()
    val driverPhoneNumber: LiveData<Result<DriverPhoneNumberUiModel>>
        get() = _driverPhoneNumber

    private val _mutationProfile = MutableLiveData<Result<Boolean>>()
    val mutationProfile: LiveData<Result<Boolean>>
        get() = _mutationProfile

    private var foodItems = listOf<FoodItemUiModel>()
    private var merchantData: MerchantDataUiModel? = null
    private var orderId = ""
    private var orderStatusKey = ""

    var channelId: String = ""
    var goFoodOrderNumber: String = ""
    var isFromBubble = false

    init {
        viewModelScope.launch {
            orderIdFlow
                .debounce(DELAY_ORDER_STATE)
                .flatMapLatest { orderId ->
                    if (orderId.isNotBlank()) {
                        fetchOrderStatusUseCase(orderId).catch {
                            emit(Fail(it))
                        }
                    } else {
                        emptyFlow()
                    }
                }
                .flowOn(coroutineDispatchers.io)
                .collectLatest {
                    when (orderStatusKey) {
                        OrderStatusType.CANCELLED, OrderStatusType.COMPLETED -> {
                            fetchOrderCompletedLiveTracking(orderId)
                        }
                        else -> {
                            _orderLiveTrackingStatus.emit(it)
                        }
                    }
                }
        }
    }

    fun getFoodItems() = foodItems
    fun getOrderId() = orderId
    fun getMerchantData() = merchantData

    fun getOrderStatus() = orderStatusKey

    fun updateOrderId(orderId: String) {
        this.orderId = orderId
        orderIdFlow.tryEmit(this.orderId)
    }

    fun onSavedInstanceState() {
        savedStateHandle[ORDER_ID] = orderId
        savedStateHandle[GOFOOD_ORDER_NUMBER] = goFoodOrderNumber
        savedStateHandle[CHANNEL_ID] = channelId
        savedStateHandle[TokoChatValueUtil.IS_FROM_BUBBLE_KEY] = isFromBubble
    }

    fun onRestoreSavedInstanceState() {
        orderIdFlow.tryEmit(
            savedStateHandle.get<String>(ORDER_ID).orEmpty()
        )
        goFoodOrderNumber = savedStateHandle.get<String>(GOFOOD_ORDER_NUMBER).orEmpty()
        channelId = savedStateHandle.get<String>(CHANNEL_ID).orEmpty()
        isFromBubble = savedStateHandle.get<Boolean>(TokoChatValueUtil.IS_FROM_BUBBLE_KEY).orFalse()
    }

    fun fetchOrderDetail(orderId: String) {
        launchCatchError(block = {
            val orderDetailResult = withContext(coroutineDispatchers.io) {
                getTokoFoodOrderDetailUseCase.get().execute(orderId)
            }
            this@TokoFoodOrderTrackingViewModel.orderStatusKey = orderDetailResult.orderStatusKey
            this@TokoFoodOrderTrackingViewModel.foodItems =
                orderDetailResult.foodItemList.filterIsInstance<FoodItemUiModel>()
            this@TokoFoodOrderTrackingViewModel.merchantData = orderDetailResult.merchantData
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

    fun fetchUnReadChatCount() {
        viewModelScope.launch {
            try {
                getTokoChatCounterUseCase.get().fetchUnreadCount(channelId)
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    fun getUnReadChatCountFlow(): StateFlow<TokoChatResult<Int>> {
        return getTokoChatCounterUseCase.get().unreadCounterFlow
    }

    fun initGroupBooking(orderId: String) {
        try {
            getTokoChatGroupBookingUseCase.get().initGroupBookingChat(
                orderId = orderId,
                serviceType = TokoChatServiceType.TOKOFOOD
            )
        } catch (t: Throwable) {
            _mutationProfile.value = Fail(t)
        }
    }

    fun getGroupBookingFlow(): SharedFlow<TokoChatResult<String>> {
        return getTokoChatGroupBookingUseCase.get().groupBookingResultFlow
    }

    private fun fetchOrderCompletedLiveTracking(orderId: String) {
        launchCatchError(block = {
            val orderDetailResult = withContext(coroutineDispatchers.io) {
                getTokoFoodOrderDetailUseCase.get().execute(orderId)
            }
            this@TokoFoodOrderTrackingViewModel.orderStatusKey = orderDetailResult.orderStatusKey
            this@TokoFoodOrderTrackingViewModel.foodItems =
                orderDetailResult.foodItemList.filterIsInstance(FoodItemUiModel::class.java)
            this@TokoFoodOrderTrackingViewModel.merchantData = orderDetailResult.merchantData
            _orderCompletedLiveTracking.value = Success(orderDetailResult)
        }, onError = {
                _orderCompletedLiveTracking.value = Fail(it)
            })
    }

    private fun fetchOrderStatusUseCase(
        orderId: String
    ): Flow<Result<OrderStatusLiveTrackingUiModel>> {
        return flow {
            val result = getTokoFoodOrderStatusUseCase.get().execute(orderId)
            this@TokoFoodOrderTrackingViewModel.orderStatusKey = result.orderStatusKey
            emit(Success(result))
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        getTokoChatGroupBookingUseCase.get().cancel()
    }

    companion object {
        const val DELAY_ORDER_STATE = 5000L
        const val ORDER_ID = "orderId"
        const val CHANNEL_ID = "channelId"
        const val GOFOOD_ORDER_NUMBER = "goFoodOrderNumber"
    }
}
