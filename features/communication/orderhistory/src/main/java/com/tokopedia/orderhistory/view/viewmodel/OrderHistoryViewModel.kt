package com.tokopedia.orderhistory.view.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.graphql.data.GqlParam
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.data.OrderHistoryParam
import com.tokopedia.orderhistory.usecase.GetProductOrderHistoryUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import com.tokopedia.wishlistcommon.util.WishlistV2RemoteConfigRollenceUtil
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderHistoryViewModel @Inject constructor(
        private val contextProvider: CoroutineDispatchers,
        private val productHistoryUseCase: GetProductOrderHistoryUseCase,
        private val addWishListUseCase: AddWishListUseCase,
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

    fun addToWishList(productId: String, userId: String, wishlistV2ActionListener: WishlistV2ActionListener, wishListActionListener: WishListActionListener, context: Context) {
        if (WishlistV2RemoteConfigRollenceUtil.isUsingAddRemoveWishlistV2(context)) {
            addWishlistV2UseCase.setParams(productId, userId)
            addWishlistV2UseCase.execute(
                onSuccess = {
                    wishlistV2ActionListener.onSuccessAddWishlist(productId)},
                onError = {
                    wishlistV2ActionListener.onErrorAddWishList(it, productId)
                })
        } else {
            addWishListUseCase.createObservable(productId, userId, wishListActionListener)
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