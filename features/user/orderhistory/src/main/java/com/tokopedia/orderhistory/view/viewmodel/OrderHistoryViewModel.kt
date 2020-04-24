package com.tokopedia.orderhistory.view.viewmodel

import androidx.lifecycle.ViewModel
import com.tokopedia.orderhistory.data.Product
import com.tokopedia.orderhistory.usecase.GetProductOrderHistoryUseCase
import javax.inject.Inject

class OrderHistoryViewModel @Inject constructor(
        private val productHistoryUseCase: GetProductOrderHistoryUseCase
) : ViewModel() {

    fun loadProductHistory(shopId: String?) {
        if (shopId == null) return
        productHistoryUseCase.loadProductHistory(
                shopId,
                ::onSuccessGetHistoryProduct,
                ::onErrorGetHistoryProduct
        )
    }

    private fun onSuccessGetHistoryProduct(list: List<Product>) {
        println()
    }

    private fun onErrorGetHistoryProduct(throwable: Throwable) {
        println()
    }

}