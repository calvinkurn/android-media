package com.tokopedia.product.manage.feature.filter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.product.manage.feature.filter.data.mapper.ProductManageFilterMapper
import com.tokopedia.product.manage.feature.filter.data.model.FilterOptionsResponse
import com.tokopedia.product.manage.feature.filter.domain.GetProductManageFilterOptionsUseCase
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterDataUiModel
import com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel.FilterUiModel
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
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProductManageFilterViewModel @Inject constructor(
        private val getProductManageFilterOptionsUseCase: GetProductManageFilterOptionsUseCase,
        private val userSession: UserSessionInterface,
        private val dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.main) {

    private val _filterOptionsResponse = MutableLiveData<Result<FilterOptionsResponse>>()
    val filterOptionsResponse: LiveData<Result<FilterOptionsResponse>>
        get() = _filterOptionsResponse

    private val _filterData = MutableLiveData<MutableList<FilterUiModel>>()
    val filterData: LiveData<MutableList<FilterUiModel>>
        get() = _filterData

    private var selectedSort: FilterDataUiModel? = null
    private var selectedEtalase: FilterDataUiModel? = null

    fun getData(shopId: String) {
        launchCatchError(block = {
            val combinedResponse = withContext(dispatcher.io) {
                getProductManageFilterOptionsUseCase.params = GetProductManageFilterOptionsUseCase.createRequestParams(shopId, isMyShop(shopId))
                getProductManageFilterOptionsUseCase.executeOnBackground()
            }
            combinedResponse.let {
                _filterOptionsResponse.postValue(Success(it))
            }
        }) {
            _filterOptionsResponse.postValue(Fail(it))
        }
    }

    fun updateData(filterData: List<FilterUiModel>) {
        _filterData.value = (filterData.toMutableList())
        findSelectedData(filterData.subList(ITEM_SORT_INDEX, ITEM_CATEGORIES_INDEX))
    }

    fun updateSpecificData(filterUiModel: FilterUiModel, index: Int) {
        val currentValue = _filterData.value?.toMutableList()
        currentValue?.let {
            it[index] = filterUiModel
        }
        findSelectedData(listOf(filterUiModel))
        _filterData.value = currentValue
    }

    fun updateSelect(filterData: FilterDataUiModel) {
        val currentData = _filterData.value.orEmpty().toMutableList()

        if(currentData.isNotEmpty()) {
            val filterList = currentData.slice(ITEM_CATEGORIES_INDEX..ITEM_OTHER_FILTER_INDEX)
            val dataToSelect = getDataFromList(filterList, filterData)
            dataToSelect?.let { (needSort, filterUiModel, selectedFilter) ->
                selectedFilter.select = true
                if(needSort) {
                    filterUiModel.data.sortByDescending { data -> data.select }
                }
            }

            _filterData.value = currentData
        }
    }

    fun updateSelect(filterData: FilterDataUiModel, title: String) {
        val currentData = _filterData.value.orEmpty().toMutableList()

        if (title == ProductManageFilterMapper.SORT_HEADER) {
            currentData.getOrNull(ITEM_SORT_INDEX)?.let { sortData ->
                val dataToSelect = getSortFromList(sortData, filterData)
                selectedSort?.let {
                    it.select = false
                    if (it == filterData) {
                        selectedSort = null
                        _filterData.value = currentData
                        return
                    }
                }
                dataToSelect?.let {
                    it.second.select = true
                    selectedSort = it.second
                    if(it.first) {
                        sortData.data.sortByDescending { data -> data.select }
                    }
                }
            }
        } else {
            val etalaseData = currentData.subList(ITEM_ETALASE_INDEX, ITEM_CATEGORIES_INDEX)
            val dataToSelect = getDataFromList(etalaseData, filterData)
            selectedEtalase?.let {
                it.select = false
                if (it == filterData) {
                    selectedEtalase = null
                    _filterData.value = currentData
                    return
                }
            }
            dataToSelect?.let { (needSort, filterUiModel, selectedFilter) ->
                selectedFilter.select = true
                selectedEtalase = selectedFilter
                if(needSort) {
                    filterUiModel.data.sortByDescending { data -> data.select }
                }
            }
        }

        if(currentData.isNotEmpty()) {
            _filterData.value = currentData
        }
    }

    fun updateShow(filterUiModel: FilterUiModel) {
        val currentData = _filterData.value?.toMutableList()
        currentData?.let {
            val filterIndexOfData = it.indexOf(filterUiModel)
            it[filterIndexOfData].isChipsShown = !filterUiModel.isChipsShown
            _filterData.value = it
        }
    }

    fun updateShow(isShow: List<Boolean>) {
        val currentData = _filterData.value?.toMutableList()
        currentData?.let {
            it.forEachIndexed { index, filterViewModel ->
                filterViewModel.isChipsShown = isShow[index]
            }
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

    private fun getDataFromList(currentData: List<FilterUiModel>, data: FilterDataUiModel): Triple<Boolean, FilterUiModel, FilterDataUiModel>? {
        currentData.forEach { filterViewModel ->
            filterViewModel.data.forEach { filterData ->
                if (filterData.id == data.id) {
                    val needSort = filterViewModel.data.indexOf(filterData) > MAXIMUM_CHIPS - 1
                    return Triple(needSort, filterViewModel, filterData)
                }
            }
        }
        return null
    }

    private fun getSortFromList(sortData: FilterUiModel, data: FilterDataUiModel): Pair<Boolean, FilterDataUiModel>? {
        sortData.data.forEach {
            if (it.id == data.id && it.value == data.value) {
                val needSort = sortData.data.indexOf(it) > MAXIMUM_CHIPS - 1
                return Pair(needSort, it)
            }
        }
        return null
    }

    private fun findSelectedData(filterViewModels: List<FilterUiModel>) {
        filterViewModels.forEach {
            if(it.title == ProductManageFilterMapper.SORT_HEADER) {
                it.data.forEach { filterDataModel ->
                    if(filterDataModel.select) {
                        selectedSort = filterDataModel
                    }
                }
            } else {
                it.data.forEach { filterDataModel ->
                    if(filterDataModel.select) {
                        selectedEtalase = filterDataModel
                    }
                }
            }
        }
    }

    fun getSelectedFilters(): List<FilterDataUiModel> {
        return _filterData.value?.flatMap { filterDataUiModels ->
            filterDataUiModels.data.filter { filterDataUiModel ->
                filterDataUiModel.select
            }
        }.orEmpty()
    }
}