package com.tokopedia.product.manage.feature.quickedit.stock.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product.manage.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_STOCK
import com.tokopedia.product.manage.feature.quickedit.common.constant.EditProductConstant.MINIMUM_STOCK
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import javax.inject.Inject

class ProductManageQuickEditStockViewModel @Inject constructor() : ViewModel() {

    val stock: LiveData<Int>
        get() = _stock
    val status: LiveData<ProductStatus>
        get() = _status

    private val _stock = MutableLiveData<Int>()
    private val _status = MutableLiveData<ProductStatus>()

    fun updateStock(stock: Int) {
        when {
            isStockTooLow(stock) -> {
                _stock.value = MINIMUM_STOCK
            }
            isStockTooHigh(stock) -> {
                _stock.value = MAXIMUM_STOCK
            }
            else -> _stock.value = stock
        }
    }

    fun updateStatus(status: ProductStatus) {
        _status.value = status
    }

    private fun isStockTooLow(stock: Int): Boolean {
        if(stock <= MINIMUM_STOCK) {
            return true
        }
        return false
    }

    private fun isStockTooHigh(stock: Int): Boolean {
        if(stock > MAXIMUM_STOCK) {
            return true
        }
        return false
    }
}