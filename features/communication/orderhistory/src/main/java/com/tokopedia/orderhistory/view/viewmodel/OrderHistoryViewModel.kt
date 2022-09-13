package com.tokopedia.orderhistory.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.data.OrderHistoryParam
import com.tokopedia.orderhistory.usecase.GetProductOrderHistoryUseCase
import com.tokopedia.usecase.RequestParams
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
        private var addToCartUseCase: AddToCartUseCase,
) : BaseViewModel(contextProvider.main) {

    private var minOrderTime = "0"

    private val _product: MutableLiveData<Result<ChatHistoryProductResponse>> = MutableLiveData()
    val product: LiveData<Result<ChatHistoryProductResponse>> get() = _product

    fun loadProductHistory(shopId: String?) {
        if (shopId == null) return
        launchCatchError(
            block = {
                val response = productHistoryUseCase(OrderHistoryParam(shopId, minOrderTime))
                onSuccessGetHistoryProduct(response)
                minOrderTime = response.minOrderTime
            },
            onError = {
                onErrorGetHistoryProduct(it)
            }
        )
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

    fun addProductToCart(
            requestParams: RequestParams,
            onSuccessAddToCart: (data: DataModel) -> Unit,
            onError: (msg: String) -> Unit
    ) {
        launchCatchError(
                contextProvider.io,
                block = {
                    val atcResponse = addToCartUseCase.createObservable(requestParams)
                            .toBlocking()
                            .single().data
                    withContext(contextProvider.main) {
                        if (atcResponse.success == 1) {
                            onSuccessAddToCart(atcResponse)
                        } else {
                            onError(atcResponse.message.getOrNull(0) ?: "")
                        }
                    }
                },
                onError = {
                    withContext(contextProvider.io) {
                        it.message?.let { errorMsg ->
                            onError(errorMsg)
                        }
                    }
                }
        )
    }

}