package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiUseCase
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailOrderStatusCode
import com.tokopedia.buyerorderdetail.common.extension.combine
import com.tokopedia.buyerorderdetail.common.extension.get
import com.tokopedia.buyerorderdetail.common.extension.put
import com.tokopedia.buyerorderdetail.common.utils.ResourceProvider
import com.tokopedia.buyerorderdetail.di.BuyerOrderDetailGson
import com.tokopedia.buyerorderdetail.domain.models.AddToCartSingleRequestState
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderParams
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderResponse
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataParams
import com.tokopedia.buyerorderdetail.domain.models.GetP0DataRequestState
import com.tokopedia.buyerorderdetail.domain.usecases.FinishOrderUseCase
import com.tokopedia.buyerorderdetail.domain.usecases.GetP0DataUseCase
import com.tokopedia.buyerorderdetail.presentation.mapper.ActionButtonsUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.BuyerOrderDetailUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.OrderResolutionTicketStatusUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.OrderStatusUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.PGRecommendationWidgetUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.PaymentInfoUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.ProductListUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.ShipmentInfoUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.MultiATCState
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.StringRes
import com.tokopedia.buyerorderdetail.presentation.uistate.ActionButtonsUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.BuyerOrderDetailUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderResolutionTicketStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.PGRecommendationWidgetUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.PaymentInfoUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ProductListUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ShipmentInfoUiState
import com.tokopedia.cachemanager.CacheManager
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class BuyerOrderDetailViewModel @Inject constructor(
    @Named(BuyerOrderDetailMiscConstant.DAGGER_ATC_QUERY_NAME)
    private val atcMultiQuery: Lazy<String>,
    private val userSession: Lazy<UserSessionInterface>,
    private val getP0DataUseCase: Lazy<GetP0DataUseCase>,
    private val finishOrderUseCase: Lazy<FinishOrderUseCase>,
    private val atcUseCase: Lazy<AddToCartMultiUseCase>,
    private val resourceProvider: Lazy<ResourceProvider>,
    @BuyerOrderDetailGson private val gson: Lazy<Gson>
) : ViewModel() {

    companion object {
        private const val SAVED_GET_P0_DATA_REQUEST_STATE = "SAVED_GET_P0_DATA_REQUEST_STATE"
    }

    private val _finishOrderResult: MutableLiveData<Result<FinishOrderResponse.Data.FinishOrderBuyer>> = MutableLiveData()
    val finishOrderResult: LiveData<Result<FinishOrderResponse.Data.FinishOrderBuyer>>
        get() = _finishOrderResult

    private val _singleAtcResult: MutableLiveData<Pair<ProductListUiModel.ProductUiModel, Result<AtcMultiData>>> = MutableLiveData()
    val singleAtcResult: LiveData<Pair<ProductListUiModel.ProductUiModel, Result<AtcMultiData>>>
        get() = _singleAtcResult

    private val _multiAtcResult: MutableLiveData<MultiATCState> = MutableLiveData()
    val multiAtcResult: LiveData<MultiATCState>
        get() = _multiAtcResult

    private val getP0DataRequestParams: MutableSharedFlow<GetP0DataParams> = MutableSharedFlow()
    private val singleAtcRequestStates: MutableStateFlow<Map<String, AddToCartSingleRequestState>> = MutableStateFlow(mapOf())
    private val getP0DataRequestState: MutableStateFlow<GetP0DataRequestState> = MutableStateFlow(GetP0DataRequestState.Idle)
    private val actionButtonsUiState = getP0DataRequestState.mapLatest(
        ::mapActionButtonsUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ActionButtonsUiState.Loading
    )
    private val orderStatusUiState = getP0DataRequestState.mapLatest(
        ::mapOrderStatusUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = OrderStatusUiState.Loading
    )
    private val paymentInfoUiState = getP0DataRequestState.mapLatest(::mapPaymentInfoUiState)
    private val productListUiState = combine(
        getP0DataRequestState, singleAtcRequestStates, ::mapProductListUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = ProductListUiState.Loading
    )
    private val shipmentInfoUiState = getP0DataRequestState.mapLatest(::mapShipmentInfoUiState)
    private val pGRecommendationWidgetUiState = getP0DataRequestState.mapLatest(::mapPGRecommendationWidgetUiState)
    private val orderResolutionTicketStatusUiState = getP0DataRequestState.mapLatest(::mapOrderResolutionTicketStatusUiState)

    val buyerOrderDetailUiState: StateFlow<BuyerOrderDetailUiState> = combine(
        actionButtonsUiState, orderStatusUiState, paymentInfoUiState, productListUiState,
        shipmentInfoUiState, pGRecommendationWidgetUiState, orderResolutionTicketStatusUiState,
        ::mapBuyerOrderDetailUiState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = BuyerOrderDetailUiState.FullscreenLoading
    )

    init {
        viewModelScope.launch {
            getP0DataRequestParams.collectLatest { params ->
                getP0DataUseCase.get().getP0Data(params).collect { requestState ->
                    getP0DataRequestState.value = requestState
                }
            }
        }
    }

    private fun mapActionButtonsUiState(
        getP0DataRequestState: GetP0DataRequestState,
    ): ActionButtonsUiState {
        return ActionButtonsUiStateMapper.mapGetP0DataRequestStateToActionButtonsUiState(getP0DataRequestState)
    }

    private fun mapOrderStatusUiState(
        getP0DataRequestState: GetP0DataRequestState,
    ): OrderStatusUiState {
        return OrderStatusUiStateMapper.mapGetP0DataRequestStateToOrderStatusUiState(getP0DataRequestState)
    }

    private fun mapPaymentInfoUiState(
        getP0DataRequestState: GetP0DataRequestState,
    ): PaymentInfoUiState {
        return PaymentInfoUiStateMapper.mapGetP0DataRequestStateToPaymentInfoUiState(
            getP0DataRequestState, resourceProvider.get()
        )
    }

    private fun mapProductListUiState(
        getP0DataRequestState: GetP0DataRequestState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        return ProductListUiStateMapper.mapGetP0DataRequestStateToProductListUiState(
            getP0DataRequestState, singleAtcRequestStates
        )
    }

    private fun mapShipmentInfoUiState(
        getP0DataRequestState: GetP0DataRequestState
    ): ShipmentInfoUiState {
        return ShipmentInfoUiStateMapper.mapGetP0DataRequestStateToShipmentInfoUiState(
            getP0DataRequestState, resourceProvider.get()
        )
    }

    private fun mapPGRecommendationWidgetUiState(
        getP0DataRequestState: GetP0DataRequestState
    ): PGRecommendationWidgetUiState {
        return PGRecommendationWidgetUiStateMapper.mapGetP0DataRequestStateToPGRecommendationWidgetUiState(
            getP0DataRequestState
        )
    }

    private fun mapOrderResolutionTicketStatusUiState(
        getP0DataRequestState: GetP0DataRequestState
    ): OrderResolutionTicketStatusUiState {
        return OrderResolutionTicketStatusUiStateMapper.mapGetP0DataResultToOrderResolutionTicketStatusUiState(
            getP0DataRequestState
        )
    }

    private fun mapBuyerOrderDetailUiState(
        actionButtonsUiState: ActionButtonsUiState,
        orderStatusUiState: OrderStatusUiState,
        paymentInfoUiState: PaymentInfoUiState,
        productListUiState: ProductListUiState,
        shipmentInfoUiState: ShipmentInfoUiState,
        pgRecommendationWidgetUiState: PGRecommendationWidgetUiState,
        orderResolutionTicketStatusUiState: OrderResolutionTicketStatusUiState
    ): BuyerOrderDetailUiState {
        return BuyerOrderDetailUiStateMapper.map(
            actionButtonsUiState,
            orderStatusUiState,
            paymentInfoUiState,
            productListUiState,
            shipmentInfoUiState,
            pgRecommendationWidgetUiState,
            orderResolutionTicketStatusUiState,
            buyerOrderDetailUiState.value
        )
    }

    private fun getFinishOrderActionStatus(): String {
        val statusId = getOrderStatusId()
        return if (statusId.toIntOrZero() < BuyerOrderDetailOrderStatusCode.ORDER_DELIVERED) {
            BuyerOrderDetailMiscConstant.ACTION_FINISH_ORDER
        } else ""
    }

    private fun ProductListUiModel.ProductUiModel.mapToAddToCartParam(): AddToCartMultiParam {
        return AddToCartMultiParam(
            productId = productId.toLongOrZero(),
            productName = productName,
            productPrice = price.toLong(),
            qty = quantity,
            notes = productNote,
            shopId = getShopId().toIntOrZero(),
            custId = userSession.get().userId.toIntOrZero()
        )
    }

    fun getP0Data(orderId: String, paymentId: String, cart: String) {
        viewModelScope.launch {
            getP0DataRequestParams.emit(GetP0DataParams(
                cart = cart,
                orderId = orderId,
                paymentId = paymentId
            ))
        }
    }

    fun finishOrder() {
        viewModelScope.launchCatchError(block = {
            val param = FinishOrderParams(
                orderId = getOrderId(),
                userId = userSession.get().userId,
                action = getFinishOrderActionStatus()
            )
            _finishOrderResult.value = (Success(finishOrderUseCase.get().execute(param)))
        }, onError = {
            _finishOrderResult.value = (Fail(it))
        })
    }

    fun addSingleToCart(product: ProductListUiModel.ProductUiModel) {
        viewModelScope.launchCatchError(block = {
            singleAtcRequestStates.update {
                it.toMutableMap().apply {
                    put(product.productId, AddToCartSingleRequestState.Requesting)
                }
            }
            (product to atcUseCase.get().execute(
                userSession.get().userId,
                atcMultiQuery.get(),
                arrayListOf(product.mapToAddToCartParam())
            )).let { result ->
                singleAtcRequestStates.update {
                    it.toMutableMap().apply {
                        put(product.productId, AddToCartSingleRequestState.Success(result))
                    }
                }
                _singleAtcResult.value = result
            }
        }, onError = { throwable ->
            singleAtcRequestStates.update {
                it.toMutableMap().apply {
                    put(product.productId, AddToCartSingleRequestState.Error(throwable))
                }
            }
            _singleAtcResult.value = (product to Fail(throwable))
        })
    }

    fun addMultipleToCart() {
        viewModelScope.launchCatchError(block = {
            val productListUiState = productListUiState.value
            if (productListUiState is ProductListUiState.Showing) {
                val params = ArrayList(productListUiState.data.productList.map {
                    it.mapToAddToCartParam()
                })
                _multiAtcResult.value = mapMultiATCResult(atcUseCase.get().execute(userSession.get().userId, atcMultiQuery.get(), params))
            } else {
                _multiAtcResult.value = MultiATCState.Fail(message = StringRes(resourceProvider.get().getErrorMessageNoProduct()))
            }
        }, onError = {
            _multiAtcResult.value = MultiATCState.Fail(throwable = it)
        })
    }

    private fun mapMultiATCResult(result: Result<AtcMultiData>): MultiATCState {
        return when (result) {
            is Success -> MultiATCState.Success(result.data)
            is Fail -> MultiATCState.Fail(throwable = result.throwable)
        }
    }

    fun getSecondaryActionButtons(): List<ActionButtonsUiModel.ActionButton> {
        val actionButtonsUiState = actionButtonsUiState.value
        return if (actionButtonsUiState is ActionButtonsUiState.Showing) {
            actionButtonsUiState.data.secondaryActionButtons
        } else emptyList()
    }

    fun saveUiState(cacheManager: CacheManager) {
        cacheManager.put(
            customId = SAVED_GET_P0_DATA_REQUEST_STATE,
            objectToPut = getP0DataRequestState.value,
            gson = gson.get()
        )
    }

    fun restoreUiState(cacheManager: CacheManager): Boolean {
        return with(cacheManager) {
            val savedGetP0DataRequestState: GetP0DataRequestState? = get(
                customId = SAVED_GET_P0_DATA_REQUEST_STATE,
                type = GetP0DataRequestState::class.java,
                gson = gson.get()
            )
            if (
                savedGetP0DataRequestState != null &&
                savedGetP0DataRequestState is GetP0DataRequestState.CompleteState
            ) {
                getP0DataRequestState.value = savedGetP0DataRequestState
                true
            } else {
                false
            }
        }
    }

    fun getProducts(): List<ProductListUiModel.ProductUiModel> {
        val productListUiState = productListUiState.value
        return if (productListUiState is ProductListUiState.Showing) {
            productListUiState.data.productList
        } else emptyList()
    }

    fun getOrderId(): String {
        val orderStatusUiState = orderStatusUiState.value
        return if (orderStatusUiState is OrderStatusUiState.Showing) {
            orderStatusUiState.data.orderStatusHeaderUiModel.orderId
        } else "0"
    }

    fun getShopId(): String {
        val productListUiState = productListUiState.value
        return if (productListUiState is ProductListUiState.Showing) {
            productListUiState.data.productListHeaderUiModel.shopId
        } else "0"
    }

    fun getShopName(): String {
        val productListUiState = productListUiState.value
        return if (productListUiState is ProductListUiState.Showing) {
            productListUiState.data.productListHeaderUiModel.shopName
        } else ""
    }

    fun getShopType(): Int {
        val productListUiState = productListUiState.value
        return if (productListUiState is ProductListUiState.Showing) {
            productListUiState.data.productListHeaderUiModel.shopType
        } else 0
    }

    fun getCategoryId(): List<Int> {
        val categoryIdMap = HashSet<Int>()
        val productListUiState = productListUiState.value
        return if (productListUiState is ProductListUiState.Showing) {
            productListUiState.data.productList.map {
                categoryIdMap.add(it.categoryId.toIntOrZero())
            }
            productListUiState.data.productBundlingList.forEach { bundle ->
                bundle.bundleItemList.forEach {
                    categoryIdMap.add(it.categoryId.toIntOrZero())
                }
            }
            categoryIdMap.toList()
        } else emptyList()
    }

    fun getUserId(): String {
        return userSession.get().userId
    }

    fun getOrderStatusId(): String {
        val orderStatusUiState = orderStatusUiState.value
        return if (orderStatusUiState is OrderStatusUiState.Showing) {
            orderStatusUiState.data.orderStatusHeaderUiModel.orderStatusId
        } else "0"
    }
}
