package com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.shop.flashsale.common.util.DiscountUtil
import com.tokopedia.shop.flashsale.common.util.ProductErrorStatusHandler
import com.tokopedia.shop.flashsale.domain.entity.ProductSubmissionResult
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ProductionSubmissionAction
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase
import com.tokopedia.shop.flashsale.presentation.creation.manage.mapper.EditProductMapper
import com.tokopedia.shop.flashsale.presentation.creation.manage.mapper.WarehouseUiModelMapper
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.EditProductInputModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.WarehouseUiModel
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class EditProductInfoViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val productErrorStatusHandler: ProductErrorStatusHandler,
    private val warehouseUiModelMapper: WarehouseUiModelMapper,
    private val doSellerCampaignProductSubmissionUseCase: DoSellerCampaignProductSubmissionUseCase
) : BaseViewModel(dispatchers.main) {

    private var _product = MutableLiveData<SellerCampaignProductList.Product>()
    val product: LiveData<SellerCampaignProductList.Product>
        get() = _product

    private var _warehouseList = MutableLiveData<List<WarehouseUiModel>>()
    val warehouseList: LiveData<List<WarehouseUiModel>>
        get() = _warehouseList

    private var _productInputData = MutableLiveData<EditProductInputModel>()
    val productInputData: LiveData<EditProductInputModel>
        get() = _productInputData

    private var _campaignPrice = MutableLiveData<Long>()
    private var _campaignPricePercent = MutableLiveData<Long>()
    val campaignPrice = Transformations.map(_campaignPricePercent) {
        DiscountUtil.getDiscountPrice(it, product.value?.price)
    }
    val campaignPricePercent = Transformations.map(_campaignPrice) {
        DiscountUtil.getDiscountPercentThresholded(it, product.value?.price)
    }

    private var _editProductResult = MutableLiveData<ProductSubmissionResult>()
    val editProductResult: LiveData<ProductSubmissionResult>
        get() = _editProductResult

    private var _errorThrowable = MutableLiveData<Throwable>()
    val errorThrowable: LiveData<Throwable>
        get() = _errorThrowable

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    val isShopMultiloc = Transformations.map(warehouseList) {
        warehouseUiModelMapper.isShopMultiloc(it)
    }

    val validationResult = Transformations.map(productInputData) {
        productErrorStatusHandler.getErrorInputType(it)
    }

    fun setProduct(product: SellerCampaignProductList.Product) {
        val warehouseList = warehouseUiModelMapper.map(product.warehouseList)
        val inputData = EditProductMapper.mapInputData(product, warehouseList)
        _product.postValue(product)
        _warehouseList.postValue(warehouseList)
        _productInputData.postValue(inputData)
    }

    fun setProductInput(input: EditProductInputModel) {
        _productInputData.postValue(input)
    }

    fun editProduct(campaignId: String) {
        _isLoading.value = true
        launchCatchError(
            dispatchers.io,
            block = {
                val result = doSellerCampaignProductSubmissionUseCase.execute(
                    campaignId,
                    ProductionSubmissionAction.SUBMIT,
                    EditProductMapper.map(productInputData.value, warehouseList.value)
                )
                _editProductResult.postValue(result)
                _isLoading.postValue(false)
            },
            onError = { error ->
                _errorThrowable.postValue(error)
                _isLoading.postValue(false)
            }
        )
    }

    fun setWarehouseList(warehouseList: List<WarehouseUiModel>) {
        _warehouseList.postValue(warehouseList)
    }

    fun setCampaignPrice(price: Long) {
        _campaignPrice.value = price
    }

    fun setCampaignPricePercent(percent: Long) {
        _campaignPricePercent.value = percent
    }
}