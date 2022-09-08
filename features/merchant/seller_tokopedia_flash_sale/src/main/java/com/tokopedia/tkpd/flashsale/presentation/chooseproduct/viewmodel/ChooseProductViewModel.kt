package com.tokopedia.tkpd.flashsale.presentation.chooseproduct.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.campaign.components.adapter.DelegateAdapterItem
import com.tokopedia.campaign.entity.ChooseProductItem
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.tkpd.flashsale.domain.entity.CategorySelection
import com.tokopedia.tkpd.flashsale.domain.entity.CriteriaCheckingResult
import com.tokopedia.tkpd.flashsale.domain.entity.ProductReserveResult
import com.tokopedia.tkpd.flashsale.domain.usecase.DoFlashSaleProductReserveUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductCriteriaCheckingUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductListToReserveUseCase
import com.tokopedia.tkpd.flashsale.domain.usecase.GetFlashSaleProductPerCriteriaUseCase
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.FILTER_PRODUCT_CRITERIA_PASSED
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.constant.ChooseProductConstant.MAX_PER_PAGE
import com.tokopedia.tkpd.flashsale.presentation.chooseproduct.mapper.ChooseProductUiMapper
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject

class ChooseProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getFlashSaleProductListToReserveUseCase: GetFlashSaleProductListToReserveUseCase,
    private val getFlashSaleProductPerCriteriaUseCase: GetFlashSaleProductPerCriteriaUseCase,
    private val doFlashSaleProductReserveUseCase: DoFlashSaleProductReserveUseCase,
    private val getFlashSaleProductCriteriaCheckingUseCase: GetFlashSaleProductCriteriaCheckingUseCase,
    private val userSession: UserSessionInterface
) : BaseViewModel(dispatchers.main){

    private val _productList = MutableLiveData<List<ChooseProductItem>>()
    val productList: LiveData<List<ChooseProductItem>> get() = _productList

    private val _preselectedProductCount = MutableLiveData<Int>()
    val preselectedProductCount: LiveData<Int> get() = _preselectedProductCount

    private val _criteriaList = MutableLiveData<List<CategorySelection>>()
    val criteriaList: LiveData<List<CategorySelection>> get() = _criteriaList

    private val _productReserveResult = MutableLiveData<ProductReserveResult>()
    val productReserveResult: LiveData<ProductReserveResult> get() = _productReserveResult

    private val _criteriaCheckingResult = MutableLiveData<List<CriteriaCheckingResult>>()
    val criteriaCheckingResult: LiveData<List<CriteriaCheckingResult>> get() = _criteriaCheckingResult

    private val _error = MutableLiveData<Throwable>()
    val error: LiveData<Throwable> get() = _error

    private val _selectedProductList = MutableLiveData<List<DelegateAdapterItem>>()

    val categoryAllList = Transformations.map(criteriaList) {
        ChooseProductUiMapper.collectAllCategory(it)
    }

    val maxSelectedProduct = Transformations.map(criteriaList) {
        ChooseProductUiMapper.getMaxSelectedProduct(it)
    }

    val selectedProductCount = Transformations.map(_selectedProductList) {
        ChooseProductUiMapper.getSelectedProductCount(it)
    }

    val hasNextPage: Boolean get() = productList.value?.size == MAX_PER_PAGE
    var filterCriteria: String = FILTER_PRODUCT_CRITERIA_PASSED
    var filterCategory: List<Long> = emptyList()
    var campaignId: Long = 0 //829856

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
                _productList.postValue(result.productList)
                _preselectedProductCount.postValue(result.selectedProductCount)
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

    fun setSelectedProduct(items: List<DelegateAdapterItem>) {
        _selectedProductList.value = items
    }

    fun reserveProduct() {
        launchCatchError(
            dispatchers.io,
            block = {
                val reservationId = userSession.shopId + Date().time.toString()
                val param = ChooseProductUiMapper.mapToReserveParam(campaignId, reservationId, _selectedProductList.value)
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
}