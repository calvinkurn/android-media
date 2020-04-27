package com.tokopedia.orderhistory.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.orderhistory.data.ChatHistoryProductResponse
import com.tokopedia.orderhistory.data.Product
import com.tokopedia.orderhistory.usecase.GetProductOrderHistoryUseCase
import javax.inject.Inject

class OrderHistoryViewModel @Inject constructor(
        private val productHistoryUseCase: GetProductOrderHistoryUseCase
) : ViewModel() {

    private val _product: MutableLiveData<Result<List<Product>>> = MutableLiveData()
    val product: LiveData<Result<List<Product>>> get() = _product

    fun loadProductHistory(shopId: String?) {
        if (shopId == null) return
        productHistoryUseCase.loadProductHistory(
                shopId,
                ::onSuccessGetHistoryProduct,
                ::onErrorGetHistoryProduct
        )
    }

    private fun onSuccessGetHistoryProduct(orderHistory: ChatHistoryProductResponse) {
        _product.value = Result.success(orderHistory.products)
    }

    private fun onErrorGetHistoryProduct(throwable: Throwable) {
        _product.value = Result.failure(throwable)
    }

}