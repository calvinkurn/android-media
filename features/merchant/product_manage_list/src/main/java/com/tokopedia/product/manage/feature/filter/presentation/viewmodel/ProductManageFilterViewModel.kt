package com.tokopedia.product.manage.feature.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.data.model.CombinedResponse
import com.tokopedia.product.manage.feature.filter.domain.ProductManageFilterCombinedUseCase
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ProductManageFilterViewModel @Inject constructor(
        private val productManageFilterCombinedUseCase: ProductManageFilterCombinedUseCase,
        private val userSession: UserSessionInterface,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _combinedResponse = MutableLiveData<Result<CombinedResponse>>()
    val combinedResponse: LiveData<Result<CombinedResponse>>
        get() = _combinedResponse

    private val _filterData = MutableLiveData<MutableList<FilterViewModel>>()
    val filterData: LiveData<MutableList<FilterViewModel>>
        get() = _filterData

    private var selectedSort: FilterDataViewModel? = null
    private var selectedEtalase: FilterDataViewModel? = null

    fun getData(shopId: String) {
        productManageFilterCombinedUseCase.params = ProductManageFilterCombinedUseCase.createRequestParams(shopId, isMyShop(shopId))
        launchCatchError(block = {
            val combinedResponse = productManageFilterCombinedUseCase.executeOnBackground()
            combinedResponse.let {
                _combinedResponse.postValue(Success(it))
            }
        }) {
            Fail(it)
        }
    }

    fun updateData(filterData: List<FilterViewModel>) {
        _filterData.postValue(filterData.toMutableList())
    }

    fun updateSpecificData(filterViewModel: FilterViewModel, index: Int) {
        val currentValue = _filterData.value
        currentValue?.let {
            it[index] = filterViewModel
        }
        _filterData.postValue(currentValue)
    }

    fun updateSelect(filterData: FilterDataViewModel) {
        val currentData = _filterData.value
        val dataToSelect = getDataFromList(currentData, filterData)
        dataToSelect?.let {
            it.select = !filterData.select
        }
        _filterData.postValue(currentData)
    }

    fun updateSelect(filterData: FilterDataViewModel, title: String) {
        val currentData = _filterData.value
        if(title == ProductManageFilterMapper.SORT_HEADER) {
            if(selectedSort != null) {
                val selected = getDataFromList(currentData, selectedSort!!)
                selected?.let {
                    it.select = !it.select
                }
                if(selected == filterData) {
                    selectedSort = null
                    _filterData.postValue(currentData)
                    return
                }
            }
            val dataToSelect = getDataFromList(currentData, filterData)
            dataToSelect?.let {
                it.select = !filterData.select
            }
            selectedSort = dataToSelect
        } else {
            if(selectedEtalase != null) {
                val selected = getDataFromList(currentData, selectedEtalase!!)
                selected?.let {
                    it.select = !it.select
                }
                if(selected == filterData) {
                    selectedEtalase = null
                    _filterData.postValue(currentData)
                    return
                }
            }
            val dataToSelect = getDataFromList(currentData, filterData)
            dataToSelect?.let {
                it.select = !filterData.select
            }
            selectedEtalase = dataToSelect
        }
        _filterData.postValue(currentData)
    }

    fun updateShow(filterViewModel: FilterViewModel) {
        val currentData = _filterData.value
        currentData?.let {
            val filterIndexOfData = it.indexOf(filterViewModel)
            it[filterIndexOfData].isChipsShown = !filterViewModel.isChipsShown
            _filterData.postValue(it)
        }
    }

    fun clearSelected() {
        val clearedData = _filterData.value
        clearedData?.forEach {filterViewModel ->
            filterViewModel.data.forEach { filterData ->
                filterData.select = false
            }
        }
        selectedEtalase = null
        selectedSort = null
        _filterData.postValue(clearedData)
    }

    private fun isMyShop(shopId: String) = userSession.shopId == shopId

    private fun getDataFromList(currentData: List<FilterViewModel>?, data: FilterDataViewModel): FilterDataViewModel? {
        var filterIndex = 0
        var filterIndexOfData = 0
        var dataIndexOfData = 0
        currentData?.forEach {
            if(it.data.indexOf(data) != -1) {
                dataIndexOfData = it.data.indexOf(data)
                filterIndexOfData = filterIndex
            }
            filterIndex++
        }
        return currentData?.get(filterIndexOfData)?.data?.get(dataIndexOfData)
    }
}