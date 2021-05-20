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

    private val _buyerOrderDetailResult: MutableLiveData<Result<BuyerOrderDetailUiModel>> = MutableLiveData()
    val buyerOrderDetailResult: LiveData<Result<BuyerOrderDetailUiModel>>
        get() = _buyerOrderDetailResult

    private val _finishOrderResult: MutableLiveData<Result<FinishOrderResponse.Data.FinishOrderBuyer>> = MutableLiveData()
    val finishOrderResult: LiveData<Result<FinishOrderResponse.Data.FinishOrderBuyer>>
        get() = _finishOrderResult

    private val _atcResult: MutableLiveData<Pair<ProductListUiModel.ProductUiModel, Result<AtcMultiData>>> = MutableLiveData()
    val atcResult: LiveData<Pair<ProductListUiModel.ProductUiModel, Result<AtcMultiData>>>
        get() = _atcResult

    private fun getOrderId(): String {
        return buyerOrderDetailResult.value.takeIf { it is Success }?.let {
            (it as Success).data.orderStatusUiModel.orderStatusHeaderUiModel.orderId
        }.orEmpty()
    }

    private fun getFinishOrderActionStatus(): String {
        val statusId = buyerOrderDetailResult.value.takeIf { it is Success }?.let {
            (it as Success).data.orderStatusUiModel.orderStatusHeaderUiModel.orderStatusId
        }.orEmpty()
        return if (statusId.matches(Regex("\\d+")) && statusId.toInt() < 600) BuyerOrderDetailConst.ACTION_FINISH_ORDER else ""
    }

    private fun getShopId(): String {
        val buyerOrderDetailResult = _buyerOrderDetailResult.value
        return if (buyerOrderDetailResult is Success) {
            buyerOrderDetailResult.data.productListUiModel.productListHeaderUiModel.shopId
        } else "0"
    }

    fun getBuyerOrderDetail(orderId: String, paymentId: String, cart: String = "") {
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

    fun addToCart(product: ProductListUiModel.ProductUiModel) {
        launchCatchError(block = {
            val param = AddToCartMultiParam(
                    productId = product.productId.toLong(),
                    productName = product.productName,
                    productPrice = product.price.toLong(),
                    qty = product.quantity,
                    notes = product.productNote,
                    shopId = getShopId().toInt(),
                    custId = userSession.get().userId.toInt()
            )
            _atcResult.postValue(product to atcUseCase.get().execute(userSession.get().userId, atcMultiQuery.get(), arrayListOf(param)))
        }, onError = {
            _atcResult.postValue(product to Fail(it))
        })
    }

    fun getSecondaryActionButtons(): List<ActionButtonsUiModel.ActionButton> {
        val buyerOrderDetailResult = buyerOrderDetailResult.value
        return if (buyerOrderDetailResult is Success) {
            buyerOrderDetailResult.data.actionButtonsUiModel.secondaryActionButtons
        } else emptyList()
    }
}