package com.tokopedia.orderhistory.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.usecase.GetProductOrderHistoryUseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import javax.inject.Inject

class OrderHistoryViewModel @Inject constructor(
        private val contextProvider: OrderHistoryCoroutineContextProvider,
        private val productHistoryUseCase: GetProductOrderHistoryUseCase,
        private val addWishListUseCase: AddWishListUseCase
) : BaseViewModel(contextProvider.Main) {

    private val _product: MutableLiveData<Result<ChatHistoryProductResponse>> = MutableLiveData()
    val product: LiveData<Result<ChatHistoryProductResponse>> get() = _product

    fun loadProductHistory(shopId: String?) {
        if (shopId == null) return
        productHistoryUseCase.loadProductHistory(
                shopId,
                ::onSuccessGetHistoryProduct,
                ::onErrorGetHistoryProduct
        )
    }

    fun addToWishList(productId: String, userId: String?, wishListActionListener: WishListActionListener) {
        addWishListUseCase.createObservable(productId, userId, wishListActionListener)
    }

    private fun onSuccessGetHistoryProduct(orderHistory: ChatHistoryProductResponse) {
        _product.value = Success(orderHistory)
    }

    private fun onErrorGetHistoryProduct(throwable: Throwable) {
        _product.value = Fail(throwable)
    }

}