package com.tokopedia.product.manage.feature.quickedit.stock.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product.manage.feature.quickedit.stock.presentation.fragment.ProductManageQuickEditStockFragment
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import javax.inject.Inject

class ProductManageQuickEditStockViewModel @Inject constructor() : ViewModel() {

    val stock: LiveData<Int>
        get() = _stock
    val status: LiveData<ProductStatus>
        get() = _status

    private val _stock = MutableLiveData<Int>()
    private val _status = MutableLiveData<ProductStatus>()

    fun updateStock(stock: Int, isSubmit: Boolean = false) {
        when {
            isStockTooLow(stock) -> {
                _stock.value = ProductManageQuickEditStockFragment.MINIMUM_STOCK
                _status.value = ProductStatus.INACTIVE
            }
            isStockTooHigh(stock) -> {
                _stock.value = ProductManageQuickEditStockFragment.MAXIMUM_STOCK
                _status.value = ProductStatus.ACTIVE
            }
            else -> {
                _stock.value = stock
                if(!isSubmit) {
                    _status.value = ProductStatus.ACTIVE
                }
            }
        }
    }

    fun updateStatus(status: ProductStatus) {
        _status.value = status
    }

    private fun isStockTooLow(stock: Int): Boolean {
        if(stock <= ProductManageQuickEditStockFragment.MINIMUM_STOCK) {
            return true
        }
        return false
    }

    private fun isStockTooHigh(stock: Int): Boolean {
        if(stock > ProductManageQuickEditStockFragment.MAXIMUM_STOCK) {
            return true
        }
        return false
    }
}