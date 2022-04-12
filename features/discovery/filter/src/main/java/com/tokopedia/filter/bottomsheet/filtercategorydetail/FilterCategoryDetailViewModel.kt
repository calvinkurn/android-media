package com.tokopedia.filter.bottomsheet.filtercategorydetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery.common.Event
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.LevelThreeCategory
import com.tokopedia.filter.common.data.LevelTwoCategory
import com.tokopedia.filter.common.data.Option

internal class FilterCategoryDetailViewModel {

    private val headerViewModelListMutableLiveData = MutableLiveData<List<FilterCategoryLevelOneViewModel>>()
    val headerViewModelListLiveData: LiveData<List<FilterCategoryLevelOneViewModel>>
        get() = headerViewModelListMutableLiveData

    private val updateHeaderInPositionEventMutableLiveData = MutableLiveData<Event<Int>>()
    val updateHeaderInPositionEventLiveData: LiveData<Event<Int>>
        get() = updateHeaderInPositionEventMutableLiveData

    private val contentViewModelListMutableLiveData = MutableLiveData<List<FilterCategoryLevelTwoViewModel>>()
    val contentViewModelListLiveData: LiveData<List<FilterCategoryLevelTwoViewModel>>
        get() = contentViewModelListMutableLiveData

    private val updateContentInPositionEventMutableLiveData = MutableLiveData<Event<Int>>()
    val updateContentInPositionEventLiveData: LiveData<Event<Int>>
        get() = updateContentInPositionEventMutableLiveData

    private val isButtonResetVisibleMutableLiveData = MutableLiveData<Boolean>()
    val isButtonResetVisibleLiveData: LiveData<Boolean>
        get() = isButtonResetVisibleMutableLiveData

    private val isButtonSaveVisibleMutableLiveData = MutableLiveData<Boolean>()
    val isButtonSaveVisibleLiveData: LiveData<Boolean>
        get() = isButtonSaveVisibleMutableLiveData

    private var categoryFilter: Filter? = null
    private var initialSelectedCategoryFilterValue: String = ""
    var selectedCategoryFilterValue: String = ""
        private set

    private val headerViewModelList = mutableListOf<FilterCategoryLevelOneViewModel>()
    private val contentViewModelList = mutableListOf<FilterCategoryLevelTwoViewModel>()
    private var selectedHeaderPosition = 0

    fun init(filter: Filter?, selectedCategoryFilterValue: String) {
        this.categoryFilter = filter
        this.initialSelectedCategoryFilterValue = selectedCategoryFilterValue
        this.selectedCategoryFilterValue = selectedCategoryFilterValue

        isButtonResetVisibleMutableLiveData.value = isInitialSelectedCategoryFilterValueNotEmpty()
    }

    private fun isInitialSelectedCategoryFilterValueNotEmpty() = initialSelectedCategoryFilterValue.isNotEmpty()

    fun onViewCreated() {
        val categoryFilter = this.categoryFilter ?: return
        if (categoryFilter.options.isEmpty()) return

        processCategoryDetailHeader(categoryFilter)
        processCategoryDetailContent(categoryFilter)

        headerViewModelListMutableLiveData.postValue(headerViewModelList)
        contentViewModelListMutableLiveData.postValue(contentViewModelList)
    }

    private fun processCategoryDetailHeader(categoryFilter: Filter) {
        categoryFilter.options.forEachIndexed { index, it ->
            val isSelected = it.isCategoryOptionSelected() || isFirstIndexAndNoCategoryFilterSelected(index)

            if (isSelected) selectedHeaderPosition = index

            val filterCategoryHeaderViewModel = createFilterCategoryHeaderViewModel(it, isSelected)

            headerViewModelList.add(filterCategoryHeaderViewModel)
        }
    }

    private fun Option.isCategoryOptionSelected() =
            value == selectedCategoryFilterValue || levelTwoCategoryList.any { it.isLevelTwoCategorySelected() }

    private fun LevelTwoCategory.isLevelTwoCategorySelected() =
            value == selectedCategoryFilterValue || levelThreeCategoryList.any { it.isLevelThreeCategorySelected() }

    private fun LevelThreeCategory.isLevelThreeCategorySelected() =
            value == selectedCategoryFilterValue

    private fun isFirstIndexAndNoCategoryFilterSelected(index: Int): Boolean {
        return index == 0 && selectedCategoryFilterValue == ""
    }

    private fun createFilterCategoryHeaderViewModel(it: Option, isSelected: Boolean): FilterCategoryLevelOneViewModel {
        return FilterCategoryLevelOneViewModel(it).also { it.isSelected = isSelected }
    }

    private fun processCategoryDetailContent(categoryFilter: Filter) {
        contentViewModelList.clear()

        categoryFilter.options[selectedHeaderPosition].levelTwoCategoryList.forEach {
            val filterCategoryLevelTwoViewModel = createFilterCategoryLevelTwoViewModel(it)

            contentViewModelList.add(filterCategoryLevelTwoViewModel)
        }
    }

    private fun createFilterCategoryLevelTwoViewModel(levelTwoCategory: LevelTwoCategory): FilterCategoryLevelTwoViewModel {
        val levelThreeCategoryViewModelList = mutableListOf<FilterCategoryLevelThreeViewModel>()

        levelTwoCategory.levelThreeCategoryList.forEach {
            val filterCategoryLevelThreeViewModel = createFilterCategoryLevelThreeViewModel(it)
            levelThreeCategoryViewModelList.add(filterCategoryLevelThreeViewModel)
        }

        return FilterCategoryLevelTwoViewModel(levelTwoCategory, levelThreeCategoryViewModelList).also {
            it.isSelectedOrExpanded = levelTwoCategory.isLevelTwoCategorySelected()
        }
    }

    private fun createFilterCategoryLevelThreeViewModel(levelThreeCategory: LevelThreeCategory): FilterCategoryLevelThreeViewModel {
        return FilterCategoryLevelThreeViewModel(levelThreeCategory).also {
            it.isSelected = levelThreeCategory.isLevelThreeCategorySelected()
        }
    }

    fun onHeaderItemClick(clickedHeaderItem: FilterCategoryLevelOneViewModel) {
        val previousSelectedPosition = selectedHeaderPosition
        selectedHeaderPosition = headerViewModelList.indexOf(clickedHeaderItem)

        if (selectedHeaderPosition == previousSelectedPosition) return

        this.categoryFilter?.let { selectHeaderItem(clickedHeaderItem, it, previousSelectedPosition) }
    }

    private fun selectHeaderItem(clickedHeaderItem: FilterCategoryLevelOneViewModel, categoryFilter: Filter, previousSelectedPosition: Int) {
        clickedHeaderItem.isSelected = true
        processCategoryDetailContent(categoryFilter)

        updatePreviousSelectedHeaderItem(previousSelectedPosition)

        updateHeaderInPositionEventMutableLiveData.value = Event(selectedHeaderPosition)
        contentViewModelListMutableLiveData.postValue(contentViewModelList)
    }

    private fun updatePreviousSelectedHeaderItem(previousSelectedPosition: Int) {
        if (previousSelectedPosition != -1) {
            headerViewModelList[previousSelectedPosition].isSelected = false
            updateHeaderInPositionEventMutableLiveData.value = Event(previousSelectedPosition)
        }
    }

    fun onFilterCategoryClicked(clickedFilterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel) {
        selectedCategoryFilterValue = clickedFilterCategoryLevelTwoViewModel.levelTwoCategory.value

        var previousSelectedIndex = -1
        updateUnselectedToAllCategoryFilter { previousSelectedIndex = it }

        clickedFilterCategoryLevelTwoViewModel.isSelectedOrExpanded = true

        val currentSelectedIndex = contentViewModelList.indexOf(clickedFilterCategoryLevelTwoViewModel)

        notifyViewUpdate(currentSelectedIndex, previousSelectedIndex)
    }

    private fun updateUnselectedToAllCategoryFilter(updatePreviousSelectedIndex: (Int) -> Unit) {
        contentViewModelList.forEachIndexed { levelTwoIndex, levelTwoViewModel ->
            if (levelTwoViewModel.isSelectable) {
                if (levelTwoViewModel.isSelectedOrExpanded) updatePreviousSelectedIndex(levelTwoIndex)
                levelTwoViewModel.isSelectedOrExpanded = false
            } else {
                levelTwoViewModel.levelThreeCategoryViewModelList.forEach { levelThreeViewModel ->
                    if (levelThreeViewModel.isSelected) updatePreviousSelectedIndex(levelTwoIndex)
                    levelThreeViewModel.isSelected = false
                }
            }
        }
    }

    fun onFilterCategoryClicked(clickedFilterCategoryLevelThreeViewModel: FilterCategoryLevelThreeViewModel) {
        selectedCategoryFilterValue = clickedFilterCategoryLevelThreeViewModel.levelThreeCategory.value

        var previousSelectedIndex = -1
        updateUnselectedToAllCategoryFilter { previousSelectedIndex = it }

        clickedFilterCategoryLevelThreeViewModel.isSelected = true

        val currentSelectedIndex = getLevelThreePositionInContent(clickedFilterCategoryLevelThreeViewModel)

        notifyViewUpdate(currentSelectedIndex, previousSelectedIndex)
    }

    fun onResetButtonClicked() {
        selectedCategoryFilterValue = ""

        var previousSelectedIndex = -1
        updateUnselectedToAllCategoryFilter { previousSelectedIndex = it }

        notifyContentUpdatePosition(previousSelectedIndex)

        isButtonResetVisibleMutableLiveData.value = false
        isButtonSaveVisibleMutableLiveData.value = isInitialSelectedCategoryFilterValueNotEmpty()
    }

    private fun notifyViewUpdate(currentSelectedIndex: Int, previousSelectedIndex: Int) {
        if (currentSelectedIndex != previousSelectedIndex) {
            notifyContentUpdatePosition(previousSelectedIndex)
            notifyContentUpdatePosition(currentSelectedIndex)
        } else {
            notifyContentUpdatePosition(currentSelectedIndex)
        }

        isButtonSaveVisibleMutableLiveData.value = true
        isButtonResetVisibleMutableLiveData.value = true
    }

    private fun notifyContentUpdatePosition(currentSelectedIndex: Int) {
        if (currentSelectedIndex != -1) {
            updateContentInPositionEventMutableLiveData.value = Event(currentSelectedIndex)
        }
    }

    private fun getLevelThreePositionInContent(levelThreeViewModel: FilterCategoryLevelThreeViewModel) =
            contentViewModelList.indexOfFirst {
                it.levelThreeCategoryViewModelList.contains(levelThreeViewModel)
            }
}