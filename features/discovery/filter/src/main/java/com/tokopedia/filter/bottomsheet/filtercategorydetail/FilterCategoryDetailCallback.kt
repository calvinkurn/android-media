package com.tokopedia.filter.bottomsheet.filtercategorydetail

internal interface FilterCategoryDetailCallback {

    fun onLevelTwoCategoryClicked(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel, isChecked: Boolean)

    fun onLevelThreeCategoryClicked(filterCategoryLevelThreeViewModel: FilterCategoryLevelThreeViewModel, isChecked: Boolean)
}