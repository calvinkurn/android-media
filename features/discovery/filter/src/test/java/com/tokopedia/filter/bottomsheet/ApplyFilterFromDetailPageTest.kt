package com.tokopedia.filter.bottomsheet

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheetViewModel.Companion.MAX_OPTION_SIZE
import com.tokopedia.filter.bottomsheet.filter.FilterViewModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.testutils.jsonToObject
import org.junit.Test

internal class ApplyFilterFromDetailPageTest: SortFilterBottomSheetViewModelTestFixtures() {

    @Test
    fun `Apply filter with null option list should not do anything (edge cases)`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        val selectedFilter = dynamicFilterModel.data.filter[0]
        val selectedFilterViewModel = this.sortFilterList!!.findFilterViewModel(selectedFilter)!!
        `When apply filter from detail page`(selectedFilterViewModel, null)

        val expectedMapParameter = sortFilterBottomSheetViewModel.mapParameter
        val expectedSelectedFilterMap = sortFilterBottomSheetViewModel.getSelectedFilterMap()
        val expectedSelectedOptionList = listOf<Option>()
        `Then assert map parameter is as expected`(expectedMapParameter)
        `Then assert selected filter map is as expected`(expectedSelectedFilterMap)
        `Then assert option view model list selected state`(
                selectedFilterViewModel,
                expectedSelectedOptionList,
                selectedFilter.getPopularOptionsCount()
        )
    }

    @Test
    fun `Apply filter from detail page to apply filter option`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        val selectedFilter = dynamicFilterModel.data.filter[0]
        val selectedOption = selectedFilter.options[1]
        selectedOption.inputState = true.toString()
        val selectedFilterViewModel = this.sortFilterList!!.findFilterViewModel(selectedFilter)!!
        `When apply filter from detail page`(selectedFilterViewModel, selectedFilter.options)

        val expectedMapParameter = mapOf(
                selectedOption.key to selectedOption.value,
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        )
        val expectedSelectedFilterMap = mapOf(selectedOption.key to selectedOption.value)
        val expectedSelectedOptionList = listOf(selectedOption)
        `Then assert apply filter from detail page`(
                expectedMapParameter,
                expectedSelectedFilterMap,
                selectedFilterViewModel,
                expectedSelectedOptionList
        )
    }

    private fun `When apply filter from detail page`(filterViewModel: FilterViewModel, selectedOptionList: List<Option>?) {
        sortFilterBottomSheetViewModel.onApplyFilterFromDetailPage(filterViewModel, selectedOptionList)
    }

    private fun `Then assert apply filter from detail page`(
            expectedMapParameter: Map<String, String>,
            expectedSelectedFilterMap: Map<String, String>,
            selectedFilterViewModel: FilterViewModel,
            expectedSelectedOptionList: List<Option>
    ) {
        `Then assert map parameter is as expected`(expectedMapParameter)
        `Then assert selected filter map is as expected`(expectedSelectedFilterMap)
        `Then assert option view model list selected state`(selectedFilterViewModel, expectedSelectedOptionList, MAX_OPTION_SIZE)
        `Then assert option is sorted`(selectedFilterViewModel)
        `Then assert sort filter view is updated`(1)
        `Then assert button apply is shown with loading`()
        `Then assert button reset visibility`(true)
        `Then assert filter view is expanded`()
    }

    @Test
    fun `Apply filter from detail page to apply not popular filter option`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        val selectedFilter = dynamicFilterModel.data.filter[0]
        val selectedOption = selectedFilter.options.find { !it.isPopular }!!
        selectedOption.inputState = true.toString()
        val selectedFilterViewModel = this.sortFilterList!!.findFilterViewModel(selectedFilter)!!
        `When apply filter from detail page`(selectedFilterViewModel, selectedFilter.options)

        val expectedMapParameter = mapOf(
                selectedOption.key to selectedOption.value,
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        )
        val expectedSelectedOptionList = listOf(selectedOption)

        `Then assert apply filter from detail page`(
                expectedMapParameter,
                mapOf(selectedOption.key to selectedOption.value),
                selectedFilterViewModel,
                expectedSelectedOptionList
        )
    }
}