package com.tokopedia.shop.flashsale.presentation.creation.manage.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.flashsale.domain.entity.enums.ProductionSubmissionAction
import com.tokopedia.shop.flashsale.domain.usecase.DoSellerCampaignProductSubmissionUseCase
import com.tokopedia.shop.flashsale.domain.usecase.GetSellerCampaignValidatedProductListUseCase
import com.tokopedia.shop.flashsale.presentation.creation.manage.mapper.ReserveProductMapper
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.ReserveProductModel
import com.tokopedia.shop.flashsale.presentation.creation.manage.model.SelectedProductModel
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import javax.inject.Inject

class ChooseProductViewModel @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val getSellerCampaignValidatedProductListUseCase: GetSellerCampaignValidatedProductListUseCase,
    private val doSellerCampaignProductSubmissionUseCase: DoSellerCampaignProductSubmissionUseCase
) : BaseViewModel(dispatchers.main) {

    private var _reserveProductList = MutableLiveData<List<ReserveProductModel>>()
    val reserveProductList: LiveData<List<ReserveProductModel>>
        get() = _reserveProductList

    private var _errors = MutableLiveData<Throwable>()
    val errors: LiveData<Throwable>
        get() = _errors

    private var _selectedItems = MutableLiveData<List<SelectedProductModel>>(listOf())
    val selectedItems: LiveData<List<SelectedProductModel>>
        get() = _selectedItems

    private var _isAddProductSuccess = MutableLiveData<Boolean>()
    val isAddProductSuccess: LiveData<Boolean>
        get() = _isAddProductSuccess

    val isSelectionValid = Transformations.map(selectedItems) {
        ReserveProductMapper.canReserveProduct(it, previousSelectedProductCount)
    }

    val isSelectionHasVariant = Transformations.map(selectedItems) {
        ReserveProductMapper.hasVariant(it)
    }

    private var searchKeyword: String = ""
    private var previousSelectedProductCount: Int = Int.ZERO

    fun isSearching(): Boolean {
        return searchKeyword.isNotEmpty()
    }

    fun getReserveProductList(campaignId: String, page: Int) {
        launchCatchError(
            dispatchers.io,
            block = {
                val result = getSellerCampaignValidatedProductListUseCase.execute(
                    campaignId, searchKeyword, page)
                _reserveProductList.postValue(ReserveProductMapper.mapFromProductList(result))
            },
            onError = { error ->
                _errors.postValue(error)
            }
        )
    }

    fun addProduct(campaignId: String) {
        launchCatchError(
            dispatchers.io,
            block = {
                val chosenProducts = ReserveProductMapper.mapToProductDataList(selectedItems.value)
                val result = doSellerCampaignProductSubmissionUseCase.execute(
                    campaignId,
                    ProductionSubmissionAction.RESERVE,
                    chosenProducts
                )
                val isProductAddSuccess = result.errorMessage.isEmpty()
                _isAddProductSuccess.postValue(isProductAddSuccess)
                _errors.postValue(MessageErrorException(result.errorMessage))
            },
            onError = { error ->
                _errors.postValue(error)
            }
        )
    }

    fun setSearchKeyword(keyword: String) {
        searchKeyword = keyword
    }

    fun setSelectedItems(selectedItems: List<SelectedProductModel>) {
        _selectedItems.value = selectedItems
    }

    fun setPreviousSelectedProductCount(count: Int) {
        previousSelectedProductCount = count
    }
}