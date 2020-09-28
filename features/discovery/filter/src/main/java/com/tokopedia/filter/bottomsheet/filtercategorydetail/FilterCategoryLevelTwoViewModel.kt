package com.tokopedia.filter.bottomsheet.filtercategorydetail

import com.tokopedia.filter.common.data.LevelTwoCategory

internal class FilterCategoryLevelTwoViewModel(
        val levelTwoCategory: LevelTwoCategory,
        val levelThreeCategoryViewModelList: List<FilterCategoryLevelThreeViewModel>
) {

    var isSelectedOrExpanded: Boolean = false
    val isExpandable = levelThreeCategoryViewModelList.isNotEmpty()
    val isSelectable = levelThreeCategoryViewModelList.isEmpty()
}