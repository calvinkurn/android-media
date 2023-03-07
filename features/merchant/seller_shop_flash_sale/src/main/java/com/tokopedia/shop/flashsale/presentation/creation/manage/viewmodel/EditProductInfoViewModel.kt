package com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.shop.flashsale.common.util.DiscountUtil
import com.tokopedia.shop.flashsale.common.util.ProductErrorStatusHandler
import com.tokopedia.shop.flashsale.domain.entity.ProductSubmissionResult
import com.tokopedia.shop.flashsale.domain.entity.SellerCampaignProductList
import com.tokopedia.shop.flashsale.domain.entity.enums.ProductionSubmissionAction
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase
import com.tokopedia.shop.flashsale.presentation.creation.manage.mapper.EditProductMapper
import com.tokopedia.shop.flashsale.presentation.creation.manage.mapper.ManageProductMapper
import com.tokopedia.shop.flashsale.presentation.creation.manage.mapper.WarehouseUiModelMapper
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.EditProductInputModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.WarehouseUiModel
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success

class EditProductInfoViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val productErrorStatusHandler: ProductErrorStatusHandler,
    private val warehouseUiModelMapper: WarehouseUiModelMapper,
    private val doSellerCampaignProductSubmissionUseCase: DoSellerCampaignProductSubmissionUseCase
) : BaseViewModel(dispatchers.main) {

    companion object {
        private const val VALIDATE_DEBOUNCE_DURATION = 500L
    }

    private var _product = MutableLiveData<SellerCampaignProductList.Product>()
    val product: LiveData<SellerCampaignProductList.Product>
        get() = _product

    private var _warehouseList = MutableLiveData<List<WarehouseUiModel>>()
    val warehouseList: LiveData<List<WarehouseUiModel>>
        get() = _warehouseList

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

    private val _removeProductsStatus = MutableLiveData<Result<Boolean>>()
    val removeProductsStatus: LiveData<Result<Boolean>>
        get() = _removeProductsStatus

    private var _productInputData = EditProductInputModel()
    val productInputData get() = _productInputData

    private var isUsingPricePercentage = false

    private var _campaignPrice = MutableStateFlow("")
    private var _campaignPricePercent = MutableStateFlow("")
    private var _campaignStock = MutableStateFlow("")
    private var _campaignMaxOrder = MutableStateFlow("")
    private var _deleteActionExecuted = MutableStateFlow(false)

    val campaignPrice = _campaignPricePercent.map {
        DiscountUtil.getDiscountPrice(it.toLongOrZero(), product.value?.price)
    }.distinctUntilChanged().flowOn(dispatchers.computation)

    val campaignPricePercent = _campaignPrice.map {
        DiscountUtil.getDiscountPercentThresholded(it.toLongOrZero(), product.value?.price)
    }.distinctUntilChanged().flowOn(dispatchers.computation)

    @FlowPreview
    val isValid = combine(
        campaignPrice,
        _campaignPrice,
        _campaignStock,
        _campaignMaxOrder,
        _deleteActionExecuted
    ) { priceFromDiscount, price, stock, maxOrder, _ ->
        _productInputData.price = if (isUsingPricePercentage) priceFromDiscount else price.toLongOrNull()
        _productInputData.stock = stock.toLongOrNull()
        _productInputData.maxOrder = maxOrder.toIntOrNull()

        productErrorStatusHandler.getErrorInputType(_productInputData)
    }.debounce(VALIDATE_DEBOUNCE_DURATION).flowOn(dispatchers.computation)

    fun setProduct(product: SellerCampaignProductList.Product) {
        val warehouseList = warehouseUiModelMapper.map(product.warehouseList)
        val inputData = EditProductMapper.mapInputData(product, warehouseList)
        _product.postValue(product)
        _warehouseList.postValue(warehouseList)
        _productInputData = inputData
    }

    fun removeProducts(
        campaignId: Long,
        productList: List<SellerCampaignProductList.Product>
    ) {
        launchCatchError(
            dispatchers.io,
            block = {
                val campaigns = doSellerCampaignProductSubmissionUseCase.execute(
                    campaignId = campaignId.toString(),
                    productData = ManageProductMapper.mapToProductDataList(productList),
                    action = ProductionSubmissionAction.DELETE
                )
                _removeProductsStatus.postValue(Success(campaigns.isSuccess))
            },
            onError = { error ->
                _removeProductsStatus.postValue(Fail(error))
            }
        )
    }

    fun editProduct() {
        _isLoading.value = true
        launchCatchError(
            dispatchers.io,
            block = {
                val result = doSellerCampaignProductSubmissionUseCase.execute(
                    _productInputData.productMapData.campaignId,
                    ProductionSubmissionAction.SUBMIT,
                    EditProductMapper.map(_productInputData)
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

    fun setCampaignPrice(price: String) {
        _campaignPrice.value = price
    }

    fun setCampaignPricePercent(percent: String) {
        _campaignPricePercent.value = percent
    }

    fun setCampaignStock(stock: String) {
        _campaignStock.value = stock
    }

    fun setCampaignMaxOrder(maxOrder: String) {
        _campaignMaxOrder.value = maxOrder
    }

    fun setUsingPricePercentage(enabled: Boolean) {
        isUsingPricePercentage = enabled
    }

    fun setInputWarehouseId(id: String) {
        _productInputData.warehouseId = id
    }

    fun setInputOriginalStock(stock: Long) {
        _productInputData.originalStock = stock
    }

    fun setDeleteStatus(isDeleteActionExecuted: Boolean) {
        _deleteActionExecuted.value = isDeleteActionExecuted
    }
}