package com.tokopedia.product.manage.common.feature.quickedit.stock.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MAXIMUM_STOCK
import com.tokopedia.product.manage.common.feature.quickedit.common.constant.EditProductConstant.MINIMUM_STOCK
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageTicker
import com.tokopedia.product.manage.common.feature.list.data.model.ProductManageTicker.*
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductStatus
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ProductManageQuickEditStockViewModel @Inject constructor(
    private val userSession: UserSessionInterface
) : ViewModel() {

    val stock: LiveData<Int>
        get() = _stock
    val status: LiveData<ProductStatus>
        get() = _status
    val stockTicker: LiveData<ProductManageTicker>
        get() = _stockTicker

    private val _stock = MutableLiveData<Int>()
    private val _status = MutableLiveData<ProductStatus>()
    private val _stockTicker = MutableLiveData<ProductManageTicker>()

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

    fun getStockTicker(hasEditStockAccess: Boolean) {
        val multiLocationShop = userSession.isMultiLocationShop

        val tickerType = when {
            multiLocationShop && hasEditStockAccess -> MultiLocationTicker
            multiLocationShop && !hasEditStockAccess -> ManageStockNoAccessTicker
            else -> NoTicker
        }
        _stockTicker.value = tickerType
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