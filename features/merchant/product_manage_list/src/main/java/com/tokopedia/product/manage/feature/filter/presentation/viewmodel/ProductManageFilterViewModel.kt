package com.tokopedia.product.manage.feature.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionsResponse
import com.tokopedia.product.manage.feature.filter.domain.GetProductManageFilterOptionsUseCase
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataViewModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterViewModel
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ITEM_CATEGORIES_INDEX
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ITEM_ETALASE_INDEX
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ITEM_OTHER_FILTER_INDEX
import com.tokopedia.product.manage.feature.filter.presentation.fragment.ProductManageFilterFragment.Companion.ITEM_SORT_INDEX
import com.tokopedia.product.manage.feature.filter.presentation.widget.ChipsAdapter.Companion.MAXIMUM_CHIPS
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.usecase.launch_cache_error.launchCatchError
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class ProductManageFilterViewModel @Inject constructor(
        private val getProductManageFilterOptionsUseCase: GetProductManageFilterOptionsUseCase,
        private val userSession: UserSessionInterface,
        dispatcher: CoroutineDispatcher
) : BaseViewModel(dispatcher) {

    private val _combinedResponse = MutableLiveData<Result<FilterOptionsResponse>>()
    val filterOptionsResponse: LiveData<Result<FilterOptionsResponse>>
        get() = _combinedResponse

    private val _filterData = MutableLiveData<MutableList<FilterViewModel>>()
    val filterData: LiveData<MutableList<FilterViewModel>>
        get() = _filterData

    private var selectedSort: FilterDataViewModel? = null
    private var selectedEtalase: FilterDataViewModel? = null

    fun getData(shopId: String) {
        getProductManageFilterOptionsUseCase.params = GetProductManageFilterOptionsUseCase.createRequestParams(shopId, isMyShop(shopId))
        launchCatchError(block = {
            val combinedResponse = getProductManageFilterOptionsUseCase.executeOnBackground()
            combinedResponse.let {
                _combinedResponse.postValue(Success(it))
            }
        }) {
            _combinedResponse.postValue(Fail(it))
        }
    }

    fun updateData(filterData: List<FilterViewModel>) {
        _filterData.value = (filterData.toMutableList())
    }

    fun updateSpecificData(filterViewModel: FilterViewModel, index: Int) {
        val currentValue = _filterData.value?.toMutableList()
        currentValue?.let {
            it[index] = filterViewModel
        }
        _filterData.value = currentValue
    }

    fun updateSelect(filterData: FilterDataViewModel) {
        val currentData = _filterData.value?.toMutableList()
        val dataToSelect = getDataFromList(currentData?.slice(ITEM_CATEGORIES_INDEX..ITEM_OTHER_FILTER_INDEX), filterData)
        dataToSelect.third?.let {
            it.select = !filterData.select
        }
        if(dataToSelect.first) currentData?.get(dataToSelect.second)?.data?.sortByDescending { it.select }
        _filterData.value = currentData
    }

    fun updateSelect(filterData: FilterDataViewModel, title: String) {
        val currentData = _filterData.value?.toMutableList()
        if (title == ProductManageFilterMapper.SORT_HEADER) {
            if (selectedSort != null) {
                val selectedPair = getSortFromList(currentData?.get(ITEM_SORT_INDEX), selectedSort!!)
                selectedPair.second?.let {
                    it.select = !it.select
                }
                if (selectedPair.second == filterData) {
                    selectedSort = null
                    _filterData.value = currentData
                    return
                }
            }
            val dataToSelect = getSortFromList(currentData?.get(ITEM_SORT_INDEX), filterData)
            dataToSelect.second?.let {
                it.select = !filterData.select
            }
            selectedSort = dataToSelect.second
            if(dataToSelect.first) currentData?.get(ITEM_SORT_INDEX)?.data?.sortByDescending { it.select }
        } else {
            if (selectedEtalase != null) {
                val selectedPair = getDataFromList(currentData?.subList(ITEM_ETALASE_INDEX, ITEM_CATEGORIES_INDEX), selectedEtalase!!)
                selectedPair.third?.let {
                    it.select = !it.select
                }
                if (selectedPair.third == filterData) {
                    selectedEtalase = null
                    _filterData.value = currentData
                    return
                }
            }
            val dataToSelect = getDataFromList(currentData?.subList(ITEM_ETALASE_INDEX, ITEM_CATEGORIES_INDEX), filterData)
            dataToSelect.third?.let {
                it.select = !filterData.select
            }
            selectedEtalase = dataToSelect.third
            if(dataToSelect.first) currentData?.get(ITEM_ETALASE_INDEX)?.data?.sortByDescending { it.select }
        }
        _filterData.value = currentData
    }

    fun updateShow(filterViewModel: FilterViewModel) {
        val currentData = _filterData.value?.toMutableList()
        currentData?.let {
            val filterIndexOfData = it.indexOf(filterViewModel)
            it[filterIndexOfData].isChipsShown = !filterViewModel.isChipsShown
            _filterData.value = it
        }
    }

    fun clearSelected() {
        val clearedData = _filterData.value
        clearedData?.forEach { filterViewModel ->
            filterViewModel.data.forEach { filterData ->
                filterData.select = false
            }
        }
        selectedEtalase = null
        selectedSort = null
        _filterData.value = clearedData
    }

    private fun isMyShop(shopId: String) = userSession.shopId == shopId

    private fun getDataFromList(currentData: List<FilterViewModel>?, data: FilterDataViewModel): Triple<Boolean, Int, FilterDataViewModel?> {
        var filterIndexOfData = 0
        var dataIndexOfData = 0
        var needSort = false
        currentData?.forEachIndexed { index, filterViewModel ->
            filterViewModel.data.forEach { filterData ->
                if (filterData.id == data.id) {
                    dataIndexOfData = filterViewModel.data.indexOf(filterData)
                    filterIndexOfData = index
                    needSort = dataIndexOfData > MAXIMUM_CHIPS - 1
                }
            }
        }
        if(dataIndexOfData > (MAXIMUM_CHIPS - 1)) needSort = true
        return Triple(needSort, filterIndexOfData, currentData?.get(filterIndexOfData)?.data?.get(dataIndexOfData))
    }

    private fun getSortFromList(sortData: FilterViewModel?, data: FilterDataViewModel): Pair<Boolean, FilterDataViewModel?> {
        var dataIndexOfData = 0
        var needSort = false
        sortData?.data?.forEach {
            if (it.id == data.id && it.value == data.value) {
                dataIndexOfData = sortData.data.indexOf(it)
                needSort = dataIndexOfData > MAXIMUM_CHIPS - 1
            }
        }
        if(dataIndexOfData > (MAXIMUM_CHIPS - 1)) needSort = true
        return Pair(needSort, sortData?.data?.get(dataIndexOfData))
    }
}