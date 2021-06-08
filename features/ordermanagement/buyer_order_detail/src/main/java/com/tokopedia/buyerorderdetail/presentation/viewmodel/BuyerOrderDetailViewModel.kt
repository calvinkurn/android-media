package com.tokopedia.buyerorderdetail.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.request.AddToCartMultiParam
import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.atc_common.domain.usecase.AddToCartMultiUseCase
import com.tokopedia.buyerorderdetail.common.BuyerOrderDetailConst
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderParams
import com.tokopedia.buyerorderdetail.domain.models.FinishOrderResponse
import com.tokopedia.buyerorderdetail.domain.models.GetBuyerOrderDetailParams
import com.tokopedia.buyerorderdetail.domain.usecases.FinishOrderUseCase
import com.tokopedia.buyerorderdetail.domain.usecases.GetBuyerOrderDetailUseCase
import com.tokopedia.buyerorderdetail.presentation.model.ActionButtonsUiModel
import com.tokopedia.buyerorderdetail.presentation.model.BuyerOrderDetailUiModel
import com.tokopedia.buyerorderdetail.presentation.model.ProductListUiModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

class BuyerOrderDetailViewModel @Inject constructor(
        coroutineDispatchers: CoroutineDispatchers,
        @Named(BuyerOrderDetailConst.DAGGER_ATC_QUERY_NAME)
        private val atcMultiQuery: dagger.Lazy<String>,
        private val userSession: dagger.Lazy<UserSessionInterface>,
        private val getBuyerOrderDetailUseCase: dagger.Lazy<GetBuyerOrderDetailUseCase>,
        private val finishOrderUseCase: dagger.Lazy<FinishOrderUseCase>,
        private val atcUseCase: dagger.Lazy<AddToCartMultiUseCase>
) : BaseViewModel(coroutineDispatchers.io) {

    companion object {
        private const val ERROR_MESSAGE_NO_PRODUCT = "Tidak ada produk yang dapat ditambahkan ke keranjang!"
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

    private val _multiAtcResult: MutableLiveData<Result<AtcMultiData>> = MutableLiveData()
    val multiAtcResult: LiveData<Result<AtcMultiData>>
        get() = _multiAtcResult

    private fun getFinishOrderActionStatus(): String {
        val statusId = getOrderStatusId()
        return if (statusId.matches(Regex("\\d+")) && statusId.toInt() < BuyerOrderDetailConst.STATUS_CODE_ORDER_DELIVERED) BuyerOrderDetailConst.ACTION_FINISH_ORDER else ""
    }

    private fun getOrderStatusId(): String {
        val currentBuyerOrderDetailResult = buyerOrderDetailResult.value
        return if (currentBuyerOrderDetailResult is Success) {
            currentBuyerOrderDetailResult.data.orderStatusUiModel.orderStatusHeaderUiModel.orderStatusId
        } else ""
    }

    private fun ProductListUiModel.ProductUiModel.mapToAddToCartParam(): AddToCartMultiParam {
        return AddToCartMultiParam(
                productId = productId.toLong(),
                productName = productName,
                productPrice = price.toLong(),
                qty = quantity,
                notes = productNote,
                shopId = getShopId().toInt(),
                custId = userSession.get().userId.toInt()
        )
    }

    fun getBuyerOrderDetail(orderId: String, paymentId: String, cart: String) {
        launchCatchError(block = {
            val param = GetBuyerOrderDetailParams(cart, orderId, paymentId)
            _buyerOrderDetailResult.postValue(Success(getBuyerOrderDetailUseCase.get().execute(param)))
        }, onError = {
            _buyerOrderDetailResult.postValue(Fail(it))
        })
    }

    fun finishOrder() {
        launchCatchError(block = {
            val param = FinishOrderParams(
                    orderId = getOrderId(),
                    userId = userSession.get().userId,
                    action = getFinishOrderActionStatus()
            )
            _finishOrderResult.postValue(Success(finishOrderUseCase.get().execute(param)))
        }, onError = {
            _finishOrderResult.postValue(Fail(it))
        })
    }

    fun addSingleToCart(product: ProductListUiModel.ProductUiModel) {
        launchCatchError(block = {
            _singleAtcResult.postValue(product to atcUseCase.get().execute(userSession.get().userId, atcMultiQuery.get(), arrayListOf(product.mapToAddToCartParam())))
        }, onError = {
            _singleAtcResult.postValue(product to Fail(it))
        })
    }

    fun addMultipleToCart() {
        launchCatchError(block = {
            val buyerOrderDetailResult = buyerOrderDetailResult.value
            if (buyerOrderDetailResult is Success) {
                val params = ArrayList(buyerOrderDetailResult.data.productListUiModel.productList.map {
                    it.mapToAddToCartParam()
                })
                _multiAtcResult.postValue(atcUseCase.get().execute(userSession.get().userId, atcMultiQuery.get(), params))
            } else {
                _multiAtcResult.postValue(Fail(MessageErrorException(ERROR_MESSAGE_NO_PRODUCT)))
            }
        }, onError = {
            _multiAtcResult.postValue(Fail(it))
        })
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

    fun getShopType(): String {
        val buyerOrderDetailResult = _buyerOrderDetailResult.value
        return if (buyerOrderDetailResult is Success) {
            buyerOrderDetailResult.data.productListUiModel.productListHeaderUiModel.shopType
        } else ""
    }

    fun getCurrencyCode(): String {
        return getProducts().firstOrNull()?.priceText?.filter {
            it.isLetter()
        }.orEmpty()
    }

    fun getUserId(): String {
        return userSession.get().userId
    }
}