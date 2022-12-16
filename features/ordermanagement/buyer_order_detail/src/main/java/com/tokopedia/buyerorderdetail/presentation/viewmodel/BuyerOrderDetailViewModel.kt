package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiUseCase
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailOrderStatusCode
import com.tokopedia.buyerorderdetail.common.extension.combine
import com.tokopedia.buyerorderdetail.common.utils.ResourceProvider
import com.tokopedia.buyerorderdetail.domain.models.AddToCartSingleRequestState
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderParams
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderResponse
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataParams
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailDataRequestState
import com.tokopedia.buyerorderdetail.domain.usecases.FinishOrderUseCase
import com.tokopedia.buyerorderdetail.domain.usecases.GetBuyerOrderDetailDataUseCase
import com.tokopedia.buyerorderdetail.presentation.mapper.ActionButtonsUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.BuyerOrderDetailUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.EpharmacyInfoUiStateMapper
import com.tokopedia.buyerorderdetail.presentation.mapper.OrderInsuranceUiStateMapper
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
import com.tokopedia.buyerorderdetail.presentation.uistate.EpharmacyInfoUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderInsuranceUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderResolutionTicketStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.OrderStatusUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.PGRecommendationWidgetUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.PaymentInfoUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ProductListUiState
import com.tokopedia.buyerorderdetail.presentation.uistate.ShipmentInfoUiState
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import dagger.Lazy
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

class BuyerOrderDetailViewModel @Inject constructor(
    @Named(BuyerOrderDetailMiscConstant.DAGGER_ATC_QUERY_NAME)
    private val atcMultiQuery: Lazy<String>,
    private val userSession: Lazy<UserSessionInterface>,
    private val getBuyerOrderDetailDataUseCase: Lazy<GetBuyerOrderDetailDataUseCase>,
    private val finishOrderUseCase: Lazy<FinishOrderUseCase>,
    private val atcUseCase: Lazy<AddToCartMultiUseCase>,
    private val resourceProvider: Lazy<ResourceProvider>
) : ViewModel() {

    companion object {
        private const val FLOW_TIMEOUT_MILLIS = 5000L
    }

    private val _finishOrderResult = MutableLiveData<Result<FinishOrderResponse.Data.FinishOrderBuyer>>()
    val finishOrderResult: LiveData<Result<FinishOrderResponse.Data.FinishOrderBuyer>>
        get() = _finishOrderResult

    private val _singleAtcResult = MutableLiveData<Pair<ProductListUiModel.ProductUiModel, Result<AtcMultiData>>>()
    val singleAtcResult: LiveData<Pair<ProductListUiModel.ProductUiModel, Result<AtcMultiData>>>
        get() = _singleAtcResult

    private val _multiAtcResult = MutableLiveData<MultiATCState>()
    val multiAtcResult: LiveData<MultiATCState>
        get() = _multiAtcResult

    private val buyerOrderDetailDataRequestParams = MutableSharedFlow<GetBuyerOrderDetailDataParams>(replay = Int.ONE)
    private val buyerOrderDetailDataRequestState = buyerOrderDetailDataRequestParams.flatMapLatest(
        ::doGetBuyerOrderDetailData
    ).toShareFlow()
    private val singleAtcRequestStates = MutableStateFlow<Map<String, AddToCartSingleRequestState>>(mapOf())
    private val actionButtonsUiState = buyerOrderDetailDataRequestState.mapLatest(
        ::mapActionButtonsUiState
    ).toStateFlow(ActionButtonsUiState.Loading)
    private val orderStatusUiState = buyerOrderDetailDataRequestState.mapLatest(
        ::mapOrderStatusUiState
    ).toStateFlow(OrderStatusUiState.Loading)
    private val paymentInfoUiState = buyerOrderDetailDataRequestState.mapLatest(
        ::mapPaymentInfoUiState
    ).toStateFlow(PaymentInfoUiState.Loading)
    private val productListUiState = combine(
        buyerOrderDetailDataRequestState,
        singleAtcRequestStates,
        ::mapProductListUiState
    ).toStateFlow(ProductListUiState.Loading)
    private val shipmentInfoUiState = buyerOrderDetailDataRequestState.mapLatest(
        ::mapShipmentInfoUiState
    ).toStateFlow(ShipmentInfoUiState.Loading)
    private val pGRecommendationWidgetUiState = buyerOrderDetailDataRequestState.mapLatest(
        ::mapPGRecommendationWidgetUiState
    ).toStateFlow(PGRecommendationWidgetUiState.Loading)
    private val orderResolutionTicketStatusUiState = buyerOrderDetailDataRequestState.mapLatest(
        ::mapOrderResolutionTicketStatusUiState
    ).toStateFlow(OrderResolutionTicketStatusUiState.Loading)
    private val orderInsuranceUiState = buyerOrderDetailDataRequestState.mapLatest(
        ::mapOrderInsuranceUiState
    ).toStateFlow(OrderInsuranceUiState.Loading)
    private val epharmacyInfoUiState = buyerOrderDetailDataRequestState.mapLatest(
        ::mapEpharmacyInfoUiState
    ).toStateFlow(EpharmacyInfoUiState.Loading)

    val buyerOrderDetailUiState: StateFlow<BuyerOrderDetailUiState> = combine(
        actionButtonsUiState,
        orderStatusUiState,
        paymentInfoUiState,
        productListUiState,
        shipmentInfoUiState,
        pGRecommendationWidgetUiState,
        orderResolutionTicketStatusUiState,
        orderInsuranceUiState,
        epharmacyInfoUiState,
        ::mapBuyerOrderDetailUiState
    ).toStateFlow(BuyerOrderDetailUiState.FullscreenLoading)

    fun getBuyerOrderDetailData(
        orderId: String,
        paymentId: String,
        cart: String,
        shouldCheckCache: Boolean
    ) {
        viewModelScope.launch {
            buyerOrderDetailDataRequestParams.emit(
                GetBuyerOrderDetailDataParams(
                    cart = cart,
                    orderId = orderId,
                    paymentId = paymentId,
                    shouldCheckCache = shouldCheckCache
                )
            )
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
            if (productListUiState is ProductListUiState.HasData) {
                val params = ArrayList(productListUiState.data.productList.map {
                    it.mapToAddToCartParam()
                })
                _multiAtcResult.value = mapMultiATCResult(
                    atcUseCase.get().execute(userSession.get().userId, atcMultiQuery.get(), params)
                )
            } else {
                _multiAtcResult.value = MultiATCState.Fail(
                    message = StringRes(resourceProvider.get().getErrorMessageNoProduct())
                )
            }
        }, onError = {
            _multiAtcResult.value = MultiATCState.Fail(throwable = it)
        })
    }

    fun getSecondaryActionButtons(): List<ActionButtonsUiModel.ActionButton> {
        val actionButtonsUiState = actionButtonsUiState.value
        return if (actionButtonsUiState is ActionButtonsUiState.HasData) {
            actionButtonsUiState.data.secondaryActionButtons
        } else emptyList()
    }

    fun getProducts(): List<ProductListUiModel.ProductUiModel> {
        val productListUiState = productListUiState.value
        return if (productListUiState is ProductListUiState.HasData) {
            productListUiState.data.productList
        } else emptyList()
    }

    fun getOrderId(): String {
        val orderStatusUiState = orderStatusUiState.value
        return if (orderStatusUiState is OrderStatusUiState.HasData) {
            orderStatusUiState.data.orderStatusHeaderUiModel.orderId
        } else "0"
    }

    fun getShopId(): String {
        val productListUiState = productListUiState.value
        return if (productListUiState is ProductListUiState.HasData) {
            productListUiState.data.productListHeaderUiModel.shopId
        } else "0"
    }

    fun getShopName(): String {
        val productListUiState = productListUiState.value
        return if (productListUiState is ProductListUiState.HasData) {
            productListUiState.data.productListHeaderUiModel.shopName
        } else ""
    }

    fun getShopType(): Int {
        val productListUiState = productListUiState.value
        return if (productListUiState is ProductListUiState.HasData) {
            productListUiState.data.productListHeaderUiModel.shopType
        } else 0
    }

    fun getCategoryId(): List<Int> {
        val categoryIdMap = HashSet<Int>()
        val productListUiState = productListUiState.value
        return if (productListUiState is ProductListUiState.HasData) {
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
        return if (orderStatusUiState is OrderStatusUiState.HasData) {
            orderStatusUiState.data.orderStatusHeaderUiModel.orderStatusId
        } else "0"
    }

    private fun <T> Flow<T>.toStateFlow(initialValue: T) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_TIMEOUT_MILLIS),
        initialValue = initialValue
    )

    private fun <T> Flow<T>.toShareFlow() = shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(FLOW_TIMEOUT_MILLIS),
        replay = Int.ONE
    )

    private suspend fun doGetBuyerOrderDetailData(params: GetBuyerOrderDetailDataParams): Flow<GetBuyerOrderDetailDataRequestState> {
        return getBuyerOrderDetailDataUseCase.get().invoke(params)
    }

    private fun mapActionButtonsUiState(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
    ): ActionButtonsUiState {
        return ActionButtonsUiStateMapper.map(
            getBuyerOrderDetailDataRequestState,
            actionButtonsUiState.value
        )
    }

    private fun mapOrderStatusUiState(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
    ): OrderStatusUiState {
        return OrderStatusUiStateMapper.map(
            getBuyerOrderDetailDataRequestState,
            orderStatusUiState.value
        )
    }

    private fun mapPaymentInfoUiState(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
    ): PaymentInfoUiState {
        return PaymentInfoUiStateMapper.map(
            getBuyerOrderDetailDataRequestState,
            paymentInfoUiState.value,
            resourceProvider.get()
        )
    }

    private fun mapEpharmacyInfoUiState(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
    ): EpharmacyInfoUiState {
        return EpharmacyInfoUiStateMapper.map(
            getBuyerOrderDetailDataRequestState,
            epharmacyInfoUiState.value
        )
    }

    private fun mapProductListUiState(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
        singleAtcRequestStates: Map<String, AddToCartSingleRequestState>
    ): ProductListUiState {
        return ProductListUiStateMapper.map(
            getBuyerOrderDetailDataRequestState,
            productListUiState.value,
            singleAtcRequestStates
        )
    }

    private fun mapShipmentInfoUiState(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
    ): ShipmentInfoUiState {
        return ShipmentInfoUiStateMapper.map(
            getBuyerOrderDetailDataRequestState,
            shipmentInfoUiState.value,
            resourceProvider.get(),
            userSession.get()
        )
    }

    private fun mapPGRecommendationWidgetUiState(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
    ): PGRecommendationWidgetUiState {
        return PGRecommendationWidgetUiStateMapper.map(
            getBuyerOrderDetailDataRequestState,
            pGRecommendationWidgetUiState.value
        )
    }

    private fun mapOrderResolutionTicketStatusUiState(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
    ): OrderResolutionTicketStatusUiState {
        return OrderResolutionTicketStatusUiStateMapper.map(
            getBuyerOrderDetailDataRequestState,
            orderResolutionTicketStatusUiState.value
        )
    }

    private fun mapOrderInsuranceUiState(
        getBuyerOrderDetailDataRequestState: GetBuyerOrderDetailDataRequestState,
    ): OrderInsuranceUiState {
        return OrderInsuranceUiStateMapper.map(
            getBuyerOrderDetailDataRequestState,
            orderInsuranceUiState.value
        )
    }

    private fun mapBuyerOrderDetailUiState(
        actionButtonsUiState: ActionButtonsUiState,
        orderStatusUiState: OrderStatusUiState,
        paymentInfoUiState: PaymentInfoUiState,
        productListUiState: ProductListUiState,
        shipmentInfoUiState: ShipmentInfoUiState,
        pgRecommendationWidgetUiState: PGRecommendationWidgetUiState,
        orderResolutionTicketStatusUiState: OrderResolutionTicketStatusUiState,
        orderInsuranceUiState: OrderInsuranceUiState,
        epharmacyInfoUiState: EpharmacyInfoUiState
    ): BuyerOrderDetailUiState {
        return BuyerOrderDetailUiStateMapper.map(
            actionButtonsUiState,
            orderStatusUiState,
            paymentInfoUiState,
            productListUiState,
            shipmentInfoUiState,
            pgRecommendationWidgetUiState,
            orderResolutionTicketStatusUiState,
            orderInsuranceUiState,
            epharmacyInfoUiState
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

    private fun mapMultiATCResult(result: Result<AtcMultiData>): MultiATCState {
        return when (result) {
            is Success -> MultiATCState.Success(result.data)
            is Fail -> MultiATCState.Fail(throwable = result.throwable)
        }
    }
}
