package com.tokopedia.product.manage.feature.cashback.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product.manage.feature.list.view.model.ProductViewModel
import javax.inject.Inject

class ProductManageSetCashbackViewModel @Inject constructor() : ViewModel() {

    val product: LiveData<ProductViewModel>
        get() = _product

    private val _product = MutableLiveData<ProductViewModel>()

    fun updateProduct(product: ProductViewModel) {
        _product.postValue(product)
    }

    fun updateCashback(cashback: Int) {
        val updatedProduct = _product.value?.copy(cashBack = cashback)
        _product.value = updatedProduct
    }
}