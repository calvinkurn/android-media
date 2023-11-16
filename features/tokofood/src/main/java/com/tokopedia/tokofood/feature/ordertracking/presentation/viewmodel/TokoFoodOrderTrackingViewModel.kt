package com.tokopedia.tokofood.feature.ordertracking.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.tokochat.common.util.TokoChatCommonValueUtil
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
import com.tokopedia.tokofood.feature.ordertracking.presentation.uistate.TokoFoodChatCounterUiState
import com.tokopedia.tokofood.feature.ordertracking.presentation.uistate.TokoFoodGroupBookingUiState
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.update
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
) : BaseViewModel(coroutineDispatchers.main) {

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

    private val _chatCounterUiState = MutableStateFlow(TokoFoodChatCounterUiState())
    val chatCounterUiState = _chatCounterUiState.asStateFlow()

    private val _groupBookingUiState = MutableSharedFlow<TokoFoodGroupBookingUiState>()
    val groupBookingUiState = _groupBookingUiState.asSharedFlow()

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
        savedStateHandle[TokoChatCommonValueUtil.IS_FROM_BUBBLE_KEY] = isFromBubble
    }

    fun onRestoreSavedInstanceState() {
        orderIdFlow.tryEmit(
            savedStateHandle.get<String>(ORDER_ID).orEmpty()
        )
        goFoodOrderNumber = savedStateHandle.get<String>(GOFOOD_ORDER_NUMBER).orEmpty()
        channelId = savedStateHandle.get<String>(CHANNEL_ID).orEmpty()
        isFromBubble = savedStateHandle.get<Boolean>(TokoChatCommonValueUtil.IS_FROM_BUBBLE_KEY).orFalse()
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
                getTokoChatCounterUseCase
                    .get()
                    .fetchUnreadCount(channelId)
                    .collectLatest { result ->
                        when (result) {
                            is TokoChatResult.Success -> {
                                onSuccessGetCounter(result.data)
                            }
                            is TokoChatResult.Error -> {
                                onErrorGetCounter(result.throwable)
                            }
                            is TokoChatResult.Loading -> Unit // no-op
                        }
                    }
            } catch (throwable: Throwable) {
                Timber.d(throwable)
            }
        }
    }

    private fun onSuccessGetCounter(counter: Int) {
        _chatCounterUiState.update {
            it.copy(counter = counter, error = null)
        }
    }

    private fun onErrorGetCounter(throwable: Throwable) {
        _chatCounterUiState.update {
            it.copy(counter = 0, error = throwable)
        }
    }

    fun initGroupBooking(orderId: String) {
        viewModelScope.launch {
            try {
                getTokoChatGroupBookingUseCase.get().initGroupBookingChat(
                    orderId = orderId,
                    serviceType = TokoChatServiceType.TOKOFOOD
                ).collectLatest { result ->
                    when (result) {
                        is TokoChatResult.Success -> {
                            onSuccessGroupBooking(result.data)
                        }
                        is TokoChatResult.Error -> {
                            onErrorGroupBooking(result.throwable)
                        }
                        TokoChatResult.Loading -> Unit // no-op
                    }
                }
            } catch (t: Throwable) {
                _mutationProfile.value = Fail(t)
            }
        }
    }

    private suspend fun onSuccessGroupBooking(channelUrl: String) {
        _groupBookingUiState.emit(TokoFoodGroupBookingUiState(channelUrl = channelUrl))
    }

    private suspend fun onErrorGroupBooking(throwable: Throwable) {
        _groupBookingUiState.emit(TokoFoodGroupBookingUiState(error = throwable))
        _mutationProfile.value = Fail(throwable)
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

    companion object {
        const val DELAY_ORDER_STATE = 5000L
        const val ORDER_ID = "orderId"
        const val CHANNEL_ID = "channelId"
        const val GOFOOD_ORDER_NUMBER = "goFoodOrderNumber"
    }
}
