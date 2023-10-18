package com.tokopedia.orderhistory.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.data.OrderHistoryParam
import com.tokopedia.orderhistory.data.Product
import com.tokopedia.orderhistory.usecase.GetProductOrderHistoryUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderHistoryViewModel @Inject constructor(
    private val contextProvider: CoroutineDispatchers,
    private val productHistoryUseCase: GetProductOrderHistoryUseCase,
    private val addWishlistV2UseCase: AddToWishlistV2UseCase,
    private var addToCartUseCase: AddToCartUseCase
) : BaseViewModel(contextProvider.main) {

    private var minOrderTime = "0"

    private val _product: MutableLiveData<Result<ChatHistoryProductResponse>> = MutableLiveData()
    val product: LiveData<Result<ChatHistoryProductResponse>> get() = _product

    private val _addToCartResult = MutableLiveData<Result<Pair<DataModel, Product>>>()
    val addToCartResult: LiveData<Result<Pair<DataModel, Product>>>
        get() = _addToCartResult

    fun loadProductHistory(shopId: String?) {
        if (shopId == null) return
        viewModelScope.launch {
            try {
                val response = productHistoryUseCase(OrderHistoryParam(shopId, minOrderTime))
                onSuccessGetHistoryProduct(response)
                minOrderTime = response.minOrderTime
            } catch (throwable: Throwable) {
                onErrorGetHistoryProduct(throwable)
            }
        }
    }

    fun addToWishListV2(productId: String, userId: String, wishlistV2ActionListener: WishlistV2ActionListener) {
        launch(contextProvider.main) {
            addWishlistV2UseCase.setParams(productId, userId)
            val result = withContext(contextProvider.io) { addWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                wishlistV2ActionListener.onSuccessAddWishlist(result.data, productId)
            } else {
                val error = (result as Fail).throwable
                wishlistV2ActionListener.onErrorAddWishList(error, productId)
            }
        }
    }

    private fun onSuccessGetHistoryProduct(orderHistory: ChatHistoryProductResponse) {
        _product.value = Success(orderHistory)
    }

    private fun onErrorGetHistoryProduct(throwable: Throwable) {
        _product.value = Fail(throwable)
    }

    fun addProductToCart(requestParams: AddToCartRequestParams, product: Product) {
        viewModelScope.launch {
            withContext(contextProvider.io) {
                try {
                    addToCartUseCase.setParams(requestParams)
                    val atcResponse = addToCartUseCase.executeOnBackground().data
                    if (atcResponse.success == Int.ONE) {
                        _addToCartResult.postValue(
                            Success(Pair(atcResponse, product))
                        )
                    } else {
                        _addToCartResult.postValue(
                            Fail(Throwable(atcResponse.message.getOrNull(0) ?: ""))
                        )
                    }
                } catch (throwable: Throwable) {
                    throwable.message?.let { errorMsg ->
                        _addToCartResult.postValue(Fail(Throwable(errorMsg)))
                    }
                }
            }
        }
    }
}
