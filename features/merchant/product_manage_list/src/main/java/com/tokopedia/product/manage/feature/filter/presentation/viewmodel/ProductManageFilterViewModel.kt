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

    private val _filterOptionsResponse = MutableLiveData<Result<FilterOptionsResponse>>()
    val filterOptionsResponse: LiveData<Result<FilterOptionsResponse>>
        get() = _filterOptionsResponse

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
                _filterOptionsResponse.postValue(Success(it))
            }
        }) {
            _filterOptionsResponse.postValue(Fail(it))
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
        dataToSelect?.let {
            it.third.select = !filterData.select
            if(it.first) {
                currentData?.get(dataToSelect.second)?.data?.sortByDescending { data -> data.select }
            }
        }
        _filterData.value = currentData
    }

    fun updateSelect(filterData: FilterDataViewModel, title: String) {
        val currentData = _filterData.value?.toMutableList()
        if (title == ProductManageFilterMapper.SORT_HEADER) {
            selectedSort?.let {
                val selectedPair = getSortFromList(currentData?.get(ITEM_SORT_INDEX), it)
                selectedPair?.let { pair ->
                    pair.second.select = !pair.second.select
                    if (pair.second == filterData) {
                        selectedSort = null
                        _filterData.value = currentData
                        return
                    }
                }
            }
            val dataToSelect = getSortFromList(currentData?.get(ITEM_SORT_INDEX), filterData)
            dataToSelect?.let {
                it.second.select = !filterData.select
                selectedSort = it.second
                if(it.first) {
                    currentData?.get(ITEM_SORT_INDEX)?.data?.sortByDescending { data -> data.select }
                }
            }
        } else {
            selectedEtalase?.let {
                val selectedPair = getDataFromList(currentData?.subList(ITEM_ETALASE_INDEX, ITEM_CATEGORIES_INDEX), it)
                selectedPair?.let { triple ->
                    triple.third.select = !triple.third.select
                    if (triple.third == filterData) {
                        selectedEtalase = null
                        _filterData.value = currentData
                        return
                    }
                }
            }
            val dataToSelect = getDataFromList(currentData?.subList(ITEM_ETALASE_INDEX, ITEM_CATEGORIES_INDEX), filterData)
            dataToSelect?.let {
                it.third.select = !filterData.select
                selectedEtalase = it.third
                if(it.first) {
                    currentData?.get(ITEM_ETALASE_INDEX)?.data?.sortByDescending { data -> data.select }
                }
            }
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

    private fun getDataFromList(currentData: List<FilterViewModel>?, data: FilterDataViewModel): Triple<Boolean, Int, FilterDataViewModel>? {
        currentData?.forEachIndexed { index, filterViewModel ->
            filterViewModel.data.forEach { filterData ->
                if (filterData.id == data.id) {
                    val needSort = filterViewModel.data.indexOf(filterData) > MAXIMUM_CHIPS - 1
                    return Triple(needSort, index + ITEM_CATEGORIES_INDEX, filterData)
                }
            }
        }
        return null
    }

    private fun getSortFromList(sortData: FilterViewModel?, data: FilterDataViewModel): Pair<Boolean, FilterDataViewModel>? {
        sortData?.data?.forEach {
            if (it.id == data.id && it.value == data.value) {
                val needSort = sortData.data.indexOf(it) > MAXIMUM_CHIPS - 1
                return Pair(needSort, it)
            }
        }
        return null
    }
}