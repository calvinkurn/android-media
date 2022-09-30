package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiUseCase
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailMiscConstant
import com.tokopedia.buyerorderdetail.common.constants.BuyerOrderDetailOrderStatusCode
import com.tokopedia.buyerorderdetail.common.utils.ResourceProvider
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderParams
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderResponse
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailParams
import com.tokopedia.buyerorderdetail.domain.usecases.FinishOrderUseCase
import com.tokopedia.buyerorderdetail.domain.usecases.GetDetailWithResolutionUseCase
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.MultiATCState
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.buyerorderdetail.presentation.model.StringRes
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

class BuyerOrderDetailViewModel @Inject constructor(
        coroutineDispatchers: CoroutineDispatchers,
        @Named(BuyerOrderDetailMiscConstant.DAGGER_ATC_QUERY_NAME)
        private val atcMultiQuery: dagger.Lazy<String>,
        private val userSession: dagger.Lazy<UserSessionInterface>,
        private val getBuyerOrderDetailUseCase: dagger.Lazy<GetDetailWithResolutionUseCase>,
        private val finishOrderUseCase: dagger.Lazy<FinishOrderUseCase>,
        private val atcUseCase: dagger.Lazy<AddToCartMultiUseCase>,
        private val resourceProvider: dagger.Lazy<ResourceProvider>
) : BaseViewModel(coroutineDispatchers.main) {

    companion object {
        const val STATUS_ID_REGEX_PATTERN = "\\d+"
    }

    private val _buyerOrderDetailResult: MutableLiveData<Result<BuyerOrderDetailUiModel>> = MutableLiveData()
    val buyerOrderDetailResult: LiveData<Result<BuyerOrderDetailUiModel>>
        get() = _buyerOrderDetailResult

    private val _finishOrderResult: MutableLiveData<Result<FinishOrderResponse.Data.FinishOrderBuyer>> = MutableLiveData()
    val finishOrderResult: LiveData<Result<FinishOrderResponse.Data.FinishOrderBuyer>>
        get() = _finishOrderResult

    private val _singleAtcResult: MutableLiveData<Pair<ProductListUiModel.ProductUiModel, Result<AtcMultiData>>> = MutableLiveData()
    val singleAtcResult: LiveData<Pair<ProductListUiModel.ProductUiModel, Result<AtcMultiData>>>
        get() = _singleAtcResult

    private val _multiAtcResult: MutableLiveData<MultiATCState> = MutableLiveData()
    val multiAtcResult: LiveData<MultiATCState>
        get() = _multiAtcResult

    private fun getFinishOrderActionStatus(): String {
        val statusId = getOrderStatusId()
        return if (statusId.matches(Regex(STATUS_ID_REGEX_PATTERN)) && statusId.toIntOrZero() < BuyerOrderDetailOrderStatusCode.ORDER_DELIVERED) BuyerOrderDetailMiscConstant.ACTION_FINISH_ORDER else ""
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

    fun getBuyerOrderDetail(orderId: String, paymentId: String, cart: String) {
        launchCatchError(block = {
            val param = GetBuyerOrderDetailParams(cart, orderId, paymentId)
            _buyerOrderDetailResult.value = Success(getBuyerOrderDetailUseCase.get().execute(param))
        }, onError = {
            _buyerOrderDetailResult.value = (Fail(it))
        })
    }

    fun finishOrder() {
        launchCatchError(block = {
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
        launchCatchError(block = {
            _singleAtcResult.value = (product to atcUseCase.get().execute(userSession.get().userId, atcMultiQuery.get(), arrayListOf(product.mapToAddToCartParam())))
        }, onError = {
            _singleAtcResult.value = (product to Fail(it))
        })
    }

    fun addMultipleToCart() {
        launchCatchError(block = {
            val buyerOrderDetailResult = buyerOrderDetailResult.value
            if (buyerOrderDetailResult is Success) {
                val params = ArrayList(buyerOrderDetailResult.data.productListUiModel.productList.map {
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
        val buyerOrderDetailResult = buyerOrderDetailResult.value
        return if (buyerOrderDetailResult is Success) {
            buyerOrderDetailResult.data.actionButtonsUiModel.secondaryActionButtons
        } else emptyList()
    }

    fun restoreBuyerOrderDetailData(savedBuyerOrderDetailData: BuyerOrderDetailUiModel) {
        _buyerOrderDetailResult.value = Success(savedBuyerOrderDetailData)
    }

    fun getProducts(): List<ProductListUiModel.ProductUiModel> {
        val buyerOrderDetailResult = buyerOrderDetailResult.value
        return if (buyerOrderDetailResult is Success) {
            buyerOrderDetailResult.data.productListUiModel.productList
        } else emptyList()
    }

    fun getOrderId(): String {
        val currentBuyerOrderDetailResult = buyerOrderDetailResult.value
        return if (currentBuyerOrderDetailResult is Success) {
            currentBuyerOrderDetailResult.data.orderStatusUiModel.orderStatusHeaderUiModel.orderId
        } else "0"
    }

    fun getShopId(): String {
        val buyerOrderDetailResult = _buyerOrderDetailResult.value
        return if (buyerOrderDetailResult is Success) {
            buyerOrderDetailResult.data.productListUiModel.productListHeaderUiModel.shopId
        } else "0"
    }

    fun getShopName(): String {
        val buyerOrderDetailResult = _buyerOrderDetailResult.value
        return if (buyerOrderDetailResult is Success) {
            buyerOrderDetailResult.data.productListUiModel.productListHeaderUiModel.shopName
        } else ""
    }

    fun getShopType(): Int {
        val buyerOrderDetailResult = _buyerOrderDetailResult.value
        return if (buyerOrderDetailResult is Success) {
            buyerOrderDetailResult.data.productListUiModel.productListHeaderUiModel.shopType
        } else 0
    }

    fun getCategoryId(): List<Int> {
        val categoryIdMap = HashSet<Int>()
        val buyerOrderDetailResult = _buyerOrderDetailResult.value
        return if (buyerOrderDetailResult is Success) {
            buyerOrderDetailResult.data.productListUiModel.productList.map {
                categoryIdMap.add(it.categoryId.toIntOrZero())
            }
            buyerOrderDetailResult.data.productListUiModel.productBundlingList.forEach { bundle ->
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
        val currentBuyerOrderDetailResult = buyerOrderDetailResult.value
        return if (currentBuyerOrderDetailResult is Success) {
            currentBuyerOrderDetailResult.data.orderStatusUiModel.orderStatusHeaderUiModel.orderStatusId
        } else ""
    }
}
