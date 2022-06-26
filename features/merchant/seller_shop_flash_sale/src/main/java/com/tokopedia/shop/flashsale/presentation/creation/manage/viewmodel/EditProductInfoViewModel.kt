package com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.common.util.ProductErrorStatusHandler
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.presentation.creation.manage.mapper.WarehouseUiModelMapper
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.WarehouseUiModel
import javax.inject.Inject

class EditProductInfoViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val productErrorStatusHandler: ProductErrorStatusHandler
) : BaseViewModel(dispatchers.main) {

    private var _product = MutableLiveData<SellerCampaignProductList.Product>()
    val product: LiveData<SellerCampaignProductList.Product>
        get() = _product

    private var _warehouseList = MutableLiveData<List<WarehouseUiModel>>()
    val warehouseList: LiveData<List<WarehouseUiModel>>
        get() = _warehouseList

    private var _productMapData = MutableLiveData<SellerCampaignProductList.ProductMapData>()
    val productMapData: LiveData<SellerCampaignProductList.ProductMapData>
        get() = _productMapData

    private var _errorThrowable = MutableLiveData<Throwable>()
    val errorThrowable: LiveData<Throwable>
        get() = _errorThrowable

    val hasWarehouse = Transformations.map(warehouseList) {
        it.size > 1
    }

    val validationResult = Transformations.map(productMapData) {
        productErrorStatusHandler.getErrorType(it)
    }

    fun setProduct(product: SellerCampaignProductList.Product) {
        val warehouseList = WarehouseUiModelMapper.map(product.warehouseList)
        val productMapData = product.productMapData
        _product.postValue(product)
        _warehouseList.postValue(warehouseList)
        _productMapData.postValue(productMapData)
    }

    fun setProductInput(input: SellerCampaignProductList.ProductMapData) {
        _productMapData.postValue(input)
    }

}