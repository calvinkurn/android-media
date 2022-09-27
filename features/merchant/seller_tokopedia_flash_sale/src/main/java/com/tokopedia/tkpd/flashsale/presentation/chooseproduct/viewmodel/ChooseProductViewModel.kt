package com.tokopedia.tkpd.flashsale.presentation.chooseproduct.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asFlow
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaSelection
import com.tokopedia.tkpd.flashsale.domain.entity.ProductReserveResult
import com.tokopedia.tkpd.flashsale.domain.usecase.*
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.FILTER_PRODUCT_CRITERIA_PASSED
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.MAX_PER_PAGE
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.mapper.ChooseProductUiMapper
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.flow.combine
import java.util.*
import javax.inject.Inject

class ChooseProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleProductListToReserveUseCase: GetFlashSaleProductListToReserveUseCase,
    private val getFlashSaleProductPerCriteriaUseCase: GetFlashSaleProductPerCriteriaUseCase,
    private val doFlashSaleProductReserveUseCase: DoFlashSaleProductReserveUseCase,
    private val getFlashSaleProductCriteriaCheckingUseCase: GetFlashSaleProductCriteriaCheckingUseCase,
    private val getFlashSaleListForSellerUseCase: GetFlashSaleListForSellerUseCase,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatchers.main){

    companion object{
        private const val OFFSET_FLASHSALE_INFO = 0
        private const val ROWS_FLASHSALE_INFO = 1
    }

    // General Livedata
    private val _selectedProductCount = MutableLiveData<Int>()
    val selectedProductCount: LiveData<Int> get() = _selectedProductCount

    private val _criteriaList = MutableLiveData<List<CriteriaSelection>>()
    val criteriaList: LiveData<List<CriteriaSelection>> get() = _criteriaList

    private val _productReserveResult = MutableLiveData<ProductReserveResult>()
    val productReserveResult: LiveData<ProductReserveResult> get() = _productReserveResult

    private val _criteriaCheckingResult = MutableLiveData<List<CriteriaCheckingResult>>()
    val criteriaCheckingResult: LiveData<List<CriteriaCheckingResult>> get() = _criteriaCheckingResult

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    // Selection related
    private val selectedProductList = MutableLiveData<List<ChooseProductItem>>()
    private val remoteProductList = MutableLiveData<List<ChooseProductItem>>()
    private val maxProductSubmission = MutableLiveData<Int>()

    val categoryAllList = Transformations.map(criteriaList) {
        ChooseProductUiMapper.collectAllCategory(it)
    }
    val maxSelectedProduct = Transformations.map(maxProductSubmission) {
        ChooseProductUiMapper.getMaxSelectedProduct(it)
    }
    val productList = Transformations.map(remoteProductList) {
        ChooseProductUiMapper.getSelectedProductList(selectedProductList.value, it)
    }

    val validationResult = combine(
        selectedProductCount.asFlow(), criteriaList.asFlow(), maxSelectedProduct.asFlow()
    ) { productCount, criteriaList, maxSelectedProduct ->
        ChooseProductUiMapper.validateSelection(productCount, maxSelectedProduct, criteriaList)
    }
    val selectionValidationResult = combine(
        selectedProductCount.asFlow(), criteriaList.asFlow(), maxSelectedProduct.asFlow()
    ) { selectedProductCount, criteriaList, maxSelectedProduct ->
        val isExceedMaxProduct = ChooseProductUiMapper.isExceedMaxProduct(selectedProductCount, maxSelectedProduct)
        val isExceedMaxCriteria =  ChooseProductUiMapper.isExceedMaxCriteria(criteriaList)
        Pair(isExceedMaxProduct, isExceedMaxCriteria)
    }

    // public variables
    val hasNextPage: Boolean get() = remoteProductList.value?.size == MAX_PER_PAGE
    var filterCriteria: String = FILTER_PRODUCT_CRITERIA_PASSED
    var filterCategory: List<Long> = emptyList()
    var campaignId: Long = 0
    var tabName: String = ""

    fun getProductList(page: Int, perPage: Int, keyword: String) {
        launchCatchError(
            dispatchers.io,
            block = {
                val param = GetFlashSaleProductListToReserveUseCase.Param(
                    campaignId = campaignId,
                    filterKeyword = keyword,
                    row = perPage,
                    offset = page,
                    listType = filterCriteria,
                    filterCategoryIds = filterCategory
                )
                val result = getFlashSaleProductListToReserveUseCase.execute(param)
                remoteProductList.postValue(result.productList)
                if (_selectedProductCount.value == null) _selectedProductCount.postValue(result.selectedProductCount)
            },
            onError = { error ->
                _error.postValue(error)
            }
        )
    }

    fun getCriteriaList() {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getFlashSaleProductPerCriteriaUseCase.execute(campaignId)
                _criteriaList.postValue(result)
            },
            onError = { error ->
                _error.postValue(error)
            }
        )
    }

    fun updateCriteriaList(item: ChooseProductItem) {
        _criteriaList.value = ChooseProductUiMapper.chooseCriteria(_criteriaList.value, item)
    }

    fun setSelectedProduct(item: ChooseProductItem) {
        val isSelected = item.isSelected
        if (isSelected) {
            selectedProductList.value = selectedProductList.value.orEmpty() + listOf(item)
            _selectedProductCount.value = _selectedProductCount.value?.inc()
        } else {
            selectedProductList.value = selectedProductList.value.orEmpty().filter {
                it.productId != item.productId
            }
            _selectedProductCount.value = _selectedProductCount.value?.dec()
        }
    }

    fun reserveProduct() {
        launchCatchError(
            dispatchers.io,
            block = {
                val reservationId = userSession.shopId + Date().time.toString()
                val param = ChooseProductUiMapper.mapToReserveParam(campaignId, reservationId, selectedProductList.value)
                val result = doFlashSaleProductReserveUseCase.execute(param)
                _productReserveResult.postValue(result)
            },
            onError = { error ->
                _error.postValue(error)
            }
        )
    }

    fun checkCriteria(item: ChooseProductItem) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getFlashSaleProductCriteriaCheckingUseCase.execute(
                    productId = item.productId.toLongOrZero(),
                    campaignId = campaignId,
                    productCriteriaId = item.criteriaId
                )
                _criteriaCheckingResult.postValue(result)
            },
            onError = { error ->
                _error.postValue(error)
            }
        )
    }

    fun getMaxProductSubmission() {
        launchCatchError(
            dispatchers.io,
            block = {
                val params = GetFlashSaleListForSellerUseCase.Param(
                    tabName,
                    OFFSET_FLASHSALE_INFO,
                    ROWS_FLASHSALE_INFO,
                    campaignIds = listOf(campaignId)
                )
                val response = getFlashSaleListForSellerUseCase.execute(params)
                val max = response.flashSales.firstOrNull()?.maxProductSubmission.orZero()
                maxProductSubmission.postValue(max)
                selectedProductList.postValue(listOf())
            },
            onError = { error ->
                _error.postValue(error)
            }
        )
    }
}