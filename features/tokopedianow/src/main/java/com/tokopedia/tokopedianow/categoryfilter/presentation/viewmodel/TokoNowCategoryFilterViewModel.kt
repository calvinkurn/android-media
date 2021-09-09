package com.tokopedia.tokopedianow.categoryfilter.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.tokopedianow.categoryfilter.domain.mapper.CategoryFilterMapper
import com.tokopedia.tokopedianow.categoryfilter.presentation.uimodel.CategoryFilterChip
import com.tokopedia.tokopedianow.categorylist.domain.usecase.GetCategoryListUseCase
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterUiModel.*
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowCategoryFilterViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    companion object {
        private const val CATEGORY_LEVEL_DEPTH = 2
    }

    val applyFilter: LiveData<SelectedSortFilter?>
        get() = _applyFilter
    val categoryList: LiveData<Success<List<CategoryFilterChip>>>
        get() = _categoryList
    val selectedFilter: LiveData<SelectedSortFilter?>
        get() = _selectedFilter

    private val _applyFilter = MutableLiveData<SelectedSortFilter?>()
    private val _categoryList = MutableLiveData<Success<List<CategoryFilterChip>>>()
    private var _selectedFilter = MutableLiveData<SelectedSortFilter?>()

    fun getCategoryList(warehouseId: String, selectedFilter: SelectedSortFilter?) {
        launchCatchError(block = {
            val selectedFilterIds = selectedFilter?.id.orEmpty()
            val response = getCategoryListUseCase.execute(warehouseId, CATEGORY_LEVEL_DEPTH)
            val categoryList = CategoryFilterMapper.mapToCategoryList(response, selectedFilterIds)
            _categoryList.postValue(Success(categoryList))
            _selectedFilter.postValue(selectedFilter)
        }) {

        }
    }

    fun updateSelectedFilter(filter: CategoryFilterChip) {
        val filterId = filter.id
        val filterTitle = filter.title

        val selectedFilterIds = _selectedFilter.value?.id.orEmpty().toMutableList()
        val selectedFilterTitle = _selectedFilter.value?.title.orEmpty().toMutableList()

        if (selectedFilterIds.contains(filterId)) {
            selectedFilterIds.remove(filterId)
            selectedFilterTitle.remove(filterTitle)
        } else {
            selectedFilterIds.add(filterId)
            selectedFilterTitle.add(filterTitle)
        }

        if(selectedFilterIds.isNotEmpty()) {
            val selectedSortFilter = SelectedSortFilter(
                selectedFilterIds,
                selectedFilterTitle
            )
            _selectedFilter.postValue(selectedSortFilter)
        } else {
            _selectedFilter.postValue(null)
        }
    }

    fun clearSelectedFilter() {
        _selectedFilter.postValue(null)

        val filters = getFilterList().map { categoryL1 ->
            val categoryL2List = categoryL1.childList.map { categoryL2 ->
                categoryL2.copy(chipType = ChipsUnify.TYPE_NORMAL)
            }
            categoryL1.copy(
                chipType = ChipsUnify.TYPE_NORMAL,
                childList = categoryL2List
            )
        }

        _categoryList.postValue(Success(filters))
    }

    fun setSelectedFilter(selectedFilter: SelectedSortFilter?) {
        _selectedFilter.postValue(selectedFilter)
    }

    fun applyFilter() {
        _applyFilter.postValue(_selectedFilter.value)
    }

    private fun getFilterList(): MutableList<CategoryFilterChip> {
        return _categoryList.value?.data.orEmpty().toMutableList()
    }
}