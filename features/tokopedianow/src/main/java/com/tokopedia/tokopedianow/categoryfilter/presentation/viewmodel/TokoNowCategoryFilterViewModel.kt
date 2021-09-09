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
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TokoNowCategoryFilterViewModel @Inject constructor(
    private val getCategoryListUseCase: GetCategoryListUseCase,
    dispatcher: CoroutineDispatchers
) : BaseViewModel(dispatcher.io) {

    companion object {
        private const val CATEGORY_LEVEL_DEPTH = 2
    }

    val applyFilter: LiveData<SelectedSortFilter>
        get() = _applyFilter
    val categoryList: LiveData<Success<List<CategoryFilterChip>>>
        get() = _categoryList

    private val _applyFilter = MutableLiveData<SelectedSortFilter>()
    private val _categoryList = MutableLiveData<Success<List<CategoryFilterChip>>>()

    private var selectedSortFilter: SelectedSortFilter? = SelectedSortFilter()

    fun getCategoryList(warehouseId: String) {
        launchCatchError(block = {
            val selectedFilterIds = selectedSortFilter?.id.orEmpty()
            val response = getCategoryListUseCase.execute(warehouseId, CATEGORY_LEVEL_DEPTH)
            val categoryList = CategoryFilterMapper.mapToCategoryList(response, selectedFilterIds)
            _categoryList.postValue(Success(categoryList))
        }) {

        }
    }

    fun setSelectedFilter(filter: CategoryFilterChip) {
        launchCatchError(block = {
            val filterId = filter.id.toInt()
            val filterTitle = filter.title

            val selectedFilterIds = selectedSortFilter?.id.orEmpty().toMutableList()
            val selectedFilterTitle = selectedSortFilter?.title.orEmpty().toMutableList()

            if (selectedFilterIds.contains(filterId)) {
                selectedFilterIds.remove(filterId)
                selectedFilterTitle.remove(filterTitle)
            } else {
                selectedFilterIds.add(filterId)
                selectedFilterTitle.add(filterTitle)
            }

            selectedSortFilter = SelectedSortFilter(selectedFilterIds, selectedFilterTitle)
        }) {

        }
    }

    fun setSelectedFilterIds(selectedSortFilter: SelectedSortFilter?) {
        this.selectedSortFilter = selectedSortFilter
    }

    fun applyFilter() {
        _applyFilter.postValue(selectedSortFilter)
    }
}