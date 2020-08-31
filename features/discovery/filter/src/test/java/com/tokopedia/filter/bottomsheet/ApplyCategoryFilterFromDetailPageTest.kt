package com.tokopedia.filter.bottomsheet

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheetViewModel.Companion.MAX_OPTION_SIZE
import com.tokopedia.filter.bottomsheet.filter.FilterViewModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.testutils.jsonToObject
import org.junit.Test

internal class ApplyCategoryFilterFromDetailPageTest: SortFilterBottomSheetViewModelTestFixtures() {

    @Test
    fun `Unapply filter category with empty selected value`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val categoryFilter = dynamicFilterModel.getCategoryFilter()
        val currentSelectedCategoryFilter = categoryFilter.options[0]
        val mapParameter = mapOf(currentSelectedCategoryFilter.key to currentSelectedCategoryFilter.value)
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val categoryFilterViewModel = sortFilterList!!.findFilterViewModel(categoryFilter)!!

        `When apply category filter from detail page`(categoryFilterViewModel, "")

        `Then assert apply filter from category detail page`(
                mapOf(SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE),
                mapOf(),
                categoryFilterViewModel,
                listOf(),
                categoryFilter.getPopularOptionsCount(),
                isButtonResetVisible = false
        )
    }

    private fun `When apply category filter from detail page`(categoryFilterViewModel: FilterViewModel, selectedCategoryFilterValue: String) {
        sortFilterBottomSheetViewModel.onApplyCategoryFilterFromDetailPage(categoryFilterViewModel, selectedCategoryFilterValue)
    }

    private fun `Then assert apply filter from category detail page`(
            expectedMapParameter: Map<String, String>,
            expectedSelectedFilterMap: Map<String, String>,
            selectedFilterViewModel: FilterViewModel,
            expectedSelectedOptionList: List<Option>,
            expectedOptionListSize: Int,
            isButtonResetVisible: Boolean = true
    ) {
        `Then assert map parameter is as expected`(expectedMapParameter)
        `Then assert selected filter map is as expected`(expectedSelectedFilterMap)
        `Then assert option view model list selected state`(selectedFilterViewModel, expectedSelectedOptionList, expectedOptionListSize)
        `Then assert option is sorted`(selectedFilterViewModel)
        `Then assert sort filter view is updated`(10)
        `Then assert button apply is shown with loading`()
        `Then assert button reset visibility`(isButtonResetVisible)
        `Then assert filter view is expanded`()
    }

    @Test
    fun `Apply filter category level 1`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        val categoryFilter = dynamicFilterModel.getCategoryFilter()
        val selectedCategoryOption = categoryFilter.options[5] // choose any unpopular level 1 for testing
        val selectedCategoryOptionValue = selectedCategoryOption.value
        val categoryFilterViewModel = sortFilterList!!.findFilterViewModel(categoryFilter)!!

        `When apply category filter from detail page`(categoryFilterViewModel, selectedCategoryOptionValue)

        `Then assert apply filter from category detail page`(
                mapOf(SearchApiConst.SC to selectedCategoryOptionValue, SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE),
                mapOf(SearchApiConst.SC to selectedCategoryOptionValue),
                categoryFilterViewModel,
                listOf(selectedCategoryOption),
                MAX_OPTION_SIZE
        )
    }

    @Test
    fun `Apply filter category level 2`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        val categoryFilter = dynamicFilterModel.getCategoryFilter()
        val selectedCategoryOption = categoryFilter.options[5].levelTwoCategoryList[1].asOption() // choose any unpopular level 2 for testing
        val selectedCategoryOptionValue = selectedCategoryOption.value
        val categoryFilterViewModel = sortFilterList!!.findFilterViewModel(categoryFilter)!!

        `When apply category filter from detail page`(categoryFilterViewModel, selectedCategoryOptionValue)

        `Then assert apply filter from category detail page`(
                mapOf(SearchApiConst.SC to selectedCategoryOptionValue, SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE),
                mapOf(SearchApiConst.SC to selectedCategoryOptionValue),
                categoryFilterViewModel,
                listOf(selectedCategoryOption),
                MAX_OPTION_SIZE
        )
    }

    @Test
    fun `Apply filter category level 3`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        val categoryFilter = dynamicFilterModel.getCategoryFilter()
        // choose any unpopular level 3 for testing
        val selectedCategoryOption = categoryFilter.options[5].levelTwoCategoryList[1].levelThreeCategoryList[1].asOption()
        val selectedCategoryOptionValue = selectedCategoryOption.value
        val categoryFilterViewModel = sortFilterList!!.findFilterViewModel(categoryFilter)!!

        `When apply category filter from detail page`(categoryFilterViewModel, selectedCategoryOptionValue)

        `Then assert apply filter from category detail page`(
                mapOf(SearchApiConst.SC to selectedCategoryOptionValue, SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE),
                mapOf(SearchApiConst.SC to selectedCategoryOptionValue),
                categoryFilterViewModel,
                listOf(selectedCategoryOption),
                MAX_OPTION_SIZE
        )
    }
}