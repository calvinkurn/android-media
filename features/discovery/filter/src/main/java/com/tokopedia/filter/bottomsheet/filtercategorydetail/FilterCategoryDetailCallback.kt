package com.tokopedia.filter.bottomsheet.filtercategorydetail

internal interface FilterCategoryDetailCallback {

    fun onLevelTwoCategoryClicked(filterCategoryLevelTwoViewModel: FilterCategoryLevelTwoViewModel)

    fun onLevelThreeCategoryClicked(filterCategoryLevelThreeViewModel: FilterCategoryLevelThreeViewModel)
}