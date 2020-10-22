package com.tokopedia.filter.bottomsheet

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.pricefilter.PriceFilterViewModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.toMapParam
import com.tokopedia.filter.testutils.jsonToObject
import org.junit.Test

internal class IntegrationSortFilterTest: SortFilterBottomSheetViewModelTestFixtures() {

    private val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
    private val keyword = "samsung"
    private val commonMapParameter = mapOf(
            SearchApiConst.Q to keyword,
            dynamicFilterModel.getSortKey() to dynamicFilterModel.defaultSortValue
    )

    @Test
    fun `Test sort and then filter`() {
        `Given SortFilterBottomSheet view is already created`(commonMapParameter, dynamicFilterModel)

        val clickedSort = sortFilterList!!.getAnyUnselectedSort()

        val selectedFilter = dynamicFilterModel.data.filter[0] // Just choose any filter
        val filterViewModel = sortFilterList!!.findFilterViewModel(selectedFilter)!!
        val clickedOptionViewModel = filterViewModel.getAnyUnselectedFilter()

        sortFilterBottomSheetViewModel.onSortItemClick(clickedSort)
        sortFilterBottomSheetViewModel.onOptionClick(filterViewModel, clickedOptionViewModel)
        sortFilterBottomSheetViewModel.applySortFilter()

        val expectedMapParameter = mapOf(
                SearchApiConst.Q to keyword,
                clickedSort.sort.key to clickedSort.sort.value,
                clickedOptionViewModel.option.key to clickedOptionViewModel.option.value,
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        )
        val expectedSelectedFilterMap = mapOf(
                clickedOptionViewModel.option.key to clickedOptionViewModel.option.value
        )
        val expectedSelectedSortMap = mapOf(clickedSort.sort.key to clickedSort.sort.value)
        val expectedSelectedSortName = clickedSort.sort.name

        `Then assert apply button parameters is expected`(
                expectedMapParameter, expectedSelectedFilterMap, expectedSelectedSortMap, expectedSelectedSortName
        )
    }

    private fun `Then assert apply button parameters is expected`(
            expectedMapParameter: Map<String, String>,
            expectedSelectedFilterMap: Map<String, String>,
            expectedSelectedSortMap: Map<String, String>,
            expectedSortName: String
    ) {
        `Then assert map parameter is as expected`(expectedMapParameter)
        `Then assert selected filter map is as expected`(expectedSelectedFilterMap)
        `Then assert selected sort map is as expected`(expectedSelectedSortMap)
        `Then assert selected sort name`(expectedSortName)
    }

    @Test
    fun `Test sort with apply filter then filter min price`() {
        `Given SortFilterBottomSheet view is already created`(commonMapParameter, dynamicFilterModel)

        val clickedSort = sortFilterList!!.getUnselectedSortWithApplyFilter()

        val priceFilterViewModel = sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        val selectedMinPriceFilter = 10_000

        sortFilterBottomSheetViewModel.onSortItemClick(clickedSort)
        sortFilterBottomSheetViewModel.onMinPriceFilterEdited(priceFilterViewModel, selectedMinPriceFilter)
        sortFilterBottomSheetViewModel.applySortFilter()

        val expectedMapParameter = mapOf(
                SearchApiConst.Q to keyword,
                clickedSort.sort.key to clickedSort.sort.value,
                SearchApiConst.PMIN to selectedMinPriceFilter.toString(),
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        )
        val expectedSelectedFilterMap = mapOf(SearchApiConst.PMIN to selectedMinPriceFilter.toString())
        val expectedSelectedSortMap = mapOf(clickedSort.sort.key to clickedSort.sort.value)
        val expectedSelectedSortName = clickedSort.sort.name

        `Then assert apply button parameters is expected`(
                expectedMapParameter, expectedSelectedFilterMap, expectedSelectedSortMap, expectedSelectedSortName
        )
    }

    @Test
    fun `Test filter min price then sort with apply filter`() {
        `Given SortFilterBottomSheet view is already created`(commonMapParameter, dynamicFilterModel)

        val priceFilterViewModel = sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        val selectedMinPriceFilter = 10_000

        val clickedSort = sortFilterList!!.getUnselectedSortWithApplyFilter()

        sortFilterBottomSheetViewModel.onMinPriceFilterEdited(priceFilterViewModel, selectedMinPriceFilter)
        sortFilterBottomSheetViewModel.onSortItemClick(clickedSort)
        sortFilterBottomSheetViewModel.applySortFilter()

        val expectedMapParameter = mapOf(
                SearchApiConst.Q to keyword,
                clickedSort.sort.key to clickedSort.sort.value,
                SearchApiConst.PMIN to selectedMinPriceFilter.toString(),
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_SORT_PAGE
        )
        val expectedSelectedFilterMap = mapOf(SearchApiConst.PMIN to selectedMinPriceFilter.toString())
        val expectedSelectedSortMap = mapOf(clickedSort.sort.key to clickedSort.sort.value)
        val expectedSelectedSortName = clickedSort.sort.name

        `Then assert apply button parameters is expected`(
                expectedMapParameter, expectedSelectedFilterMap, expectedSelectedSortMap, expectedSelectedSortName
        )
    }

    @Test
    fun `Test sort with apply filter then filter price range`() {
        `Given SortFilterBottomSheet view is already created`(commonMapParameter, dynamicFilterModel)

        val clickedSort = sortFilterList!!.getUnselectedSortWithApplyFilter()

        val priceFilterViewModel = sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        val clickedPriceRangeViewModel = priceFilterViewModel.priceRangeOptionViewModelList
                .find { it.option.key == Option.KEY_PRICE_RANGE_1 }!!

        sortFilterBottomSheetViewModel.onSortItemClick(clickedSort)
        sortFilterBottomSheetViewModel.onPriceRangeOptionClick(priceFilterViewModel, clickedPriceRangeViewModel)
        sortFilterBottomSheetViewModel.applySortFilter()

        val expectedMapParameter = mapOf(
                SearchApiConst.Q to keyword,
                clickedSort.sort.key to clickedSort.sort.value,
                SearchApiConst.PMIN to clickedPriceRangeViewModel.option.valMin,
                SearchApiConst.PMAX to clickedPriceRangeViewModel.option.valMax,
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE
        )
        val expectedSelectedFilterMap = mapOf(
                SearchApiConst.PMIN to clickedPriceRangeViewModel.option.valMin,
                SearchApiConst.PMAX to clickedPriceRangeViewModel.option.valMax
        )
        val expectedSelectedSortMap = mapOf(clickedSort.sort.key to clickedSort.sort.value)
        val expectedSelectedSortName = clickedSort.sort.name

        `Then assert apply button parameters is expected`(
                expectedMapParameter, expectedSelectedFilterMap, expectedSelectedSortMap, expectedSelectedSortName
        )
    }

    @Test
    fun `Test sort with apply filter then filter anything`() {
        `Given SortFilterBottomSheet view is already created`(commonMapParameter, dynamicFilterModel)

        val clickedSort = sortFilterList!!.getUnselectedSortWithApplyFilter()

        val selectedFilter = dynamicFilterModel.data.filter[0] // Just choose any filter
        val filterViewModel = sortFilterList!!.findFilterViewModel(selectedFilter)!!
        val clickedOptionViewModel = filterViewModel.getAnyUnselectedFilter()

        sortFilterBottomSheetViewModel.onSortItemClick(clickedSort)
        sortFilterBottomSheetViewModel.onOptionClick(filterViewModel, clickedOptionViewModel)
        sortFilterBottomSheetViewModel.applySortFilter()

        val expectedMapParameter = mutableMapOf(
                SearchApiConst.Q to keyword,
                clickedSort.sort.key to clickedSort.sort.value,
                clickedOptionViewModel.option.key to clickedOptionViewModel.option.value
        ).also {
            it.putAll(clickedSort.sort.applyFilter.toMapParam())
        }
        val expectedSelectedFilterMap = mapOf(
                clickedOptionViewModel.option.key to clickedOptionViewModel.option.value
        )
        val expectedSelectedSortMap = mapOf(clickedSort.sort.key to clickedSort.sort.value)
        val expectedSelectedSortName = clickedSort.sort.name

        `Then assert apply button parameters is expected`(
                expectedMapParameter, expectedSelectedFilterMap, expectedSelectedSortMap, expectedSelectedSortName
        )
    }

    /**
     * This is an edge case scenario. Usually backend team will not give "applyFilter" in Sort if there's already PMIN
     */
    @Test
    fun `Test parameter has existing filter min price and then sort with apply filter`() {
        val existingMinPriceFilterValue = 10_000.toString()
        val mapParameterWithPMIN = commonMapParameter.toMutableMap().also {
            it[SearchApiConst.PMIN] = existingMinPriceFilterValue
        }
        `Given SortFilterBottomSheet view is already created`(mapParameterWithPMIN, dynamicFilterModel)

        val clickedSort = sortFilterList!!.getUnselectedSortWithApplyFilter()

        sortFilterBottomSheetViewModel.onSortItemClick(clickedSort)
        sortFilterBottomSheetViewModel.applySortFilter()

        val expectedMapParameter = mutableMapOf(
                SearchApiConst.Q to keyword,
                clickedSort.sort.key to clickedSort.sort.value,
                SearchApiConst.PMIN to existingMinPriceFilterValue,
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_SORT_PAGE
        )
        val expectedSelectedFilterMap = mapOf(
                SearchApiConst.PMIN to existingMinPriceFilterValue
        )
        val expectedSelectedSortMap = mapOf(clickedSort.sort.key to clickedSort.sort.value)
        val expectedSelectedSortName = clickedSort.sort.name

        `Then assert apply button parameters is expected`(
                expectedMapParameter, expectedSelectedFilterMap, expectedSelectedSortMap, expectedSelectedSortName
        )
    }

    @Test
    fun `Test filter min price, reset sort and filter, then sort with apply filter`() {
        `Given SortFilterBottomSheet view is already created`(commonMapParameter, dynamicFilterModel)

        val priceFilterViewModel = sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        val selectedMinPriceFilter = 10_000

        val clickedSort = sortFilterList!!.getUnselectedSortWithApplyFilter()

        sortFilterBottomSheetViewModel.onMinPriceFilterEdited(priceFilterViewModel, selectedMinPriceFilter)
        sortFilterBottomSheetViewModel.resetSortAndFilter()
        sortFilterBottomSheetViewModel.onSortItemClick(clickedSort)
        sortFilterBottomSheetViewModel.applySortFilter()

        val expectedMapParameter = mutableMapOf(
                SearchApiConst.Q to keyword,
                clickedSort.sort.key to clickedSort.sort.value
        ).also {
            it.putAll(clickedSort.sort.applyFilter.toMapParam())
        }
        val expectedSelectedFilterMap = mapOf<String, String>()
        val expectedSelectedSortMap = mapOf(clickedSort.sort.key to clickedSort.sort.value)
        val expectedSelectedSortName = clickedSort.sort.name

        `Then assert apply button parameters is expected`(
                expectedMapParameter, expectedSelectedFilterMap, expectedSelectedSortMap, expectedSelectedSortName
        )
    }

    @Test
    fun `Test sort with apply filter, filter price range, then sort without apply filter`() {
        `Given SortFilterBottomSheet view is already created`(commonMapParameter, dynamicFilterModel)

        val clickedSortWithApplyFilter = sortFilterList!!.getUnselectedSortWithApplyFilter()
        sortFilterBottomSheetViewModel.onSortItemClick(clickedSortWithApplyFilter)

        val priceFilterViewModel = sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        val priceRangeOptionViewModel = priceFilterViewModel.getUnselectedPriceRangeOption()
        sortFilterBottomSheetViewModel.onPriceRangeOptionClick(priceFilterViewModel, priceRangeOptionViewModel)

        val clickedSortWithoutApplyFilter = sortFilterList!!.getAnyUnselectedSort()
        sortFilterBottomSheetViewModel.onSortItemClick(clickedSortWithoutApplyFilter)

        sortFilterBottomSheetViewModel.applySortFilter()

        val expectedMapParameter = mapOf(
                SearchApiConst.Q to keyword,
                clickedSortWithoutApplyFilter.sort.key to clickedSortWithoutApplyFilter.sort.value,
                SearchApiConst.PMIN to priceRangeOptionViewModel.option.valMin,
                SearchApiConst.PMAX to priceRangeOptionViewModel.option.valMax,
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_SORT_PAGE
        )
        val expectedSelectedFilterMap = mapOf(
                SearchApiConst.PMIN to priceRangeOptionViewModel.option.valMin,
                SearchApiConst.PMAX to priceRangeOptionViewModel.option.valMax
        )
        val expectedSelectedSortMap = mapOf(clickedSortWithoutApplyFilter.sort.key to clickedSortWithoutApplyFilter.sort.value)
        val expectedSelectedSortName = clickedSortWithoutApplyFilter.sort.name

        `Then assert apply button parameters is expected`(
                expectedMapParameter, expectedSelectedFilterMap, expectedSelectedSortMap, expectedSelectedSortName
        )
    }

    @Test
    fun `Test sort with apply filter, filter min price with exact value, then sort without apply filter`() {
        `Given SortFilterBottomSheet view is already created`(commonMapParameter, dynamicFilterModel)

        val clickedSortWithApplyFilter = sortFilterList!!.getUnselectedSortWithApplyFilter()
        sortFilterBottomSheetViewModel.onSortItemClick(clickedSortWithApplyFilter)

        val priceFilterViewModel = sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        val minPriceValue = 400_000
        sortFilterBottomSheetViewModel.onMinPriceFilterEdited(priceFilterViewModel, minPriceValue)

        val clickedSortWithoutApplyFilter = sortFilterList!!.getAnyUnselectedSort()
        sortFilterBottomSheetViewModel.onSortItemClick(clickedSortWithoutApplyFilter)

        sortFilterBottomSheetViewModel.applySortFilter()

        val expectedMapParameter = mapOf(
                SearchApiConst.Q to keyword,
                clickedSortWithoutApplyFilter.sort.key to clickedSortWithoutApplyFilter.sort.value,
                SearchApiConst.PMIN to minPriceValue.toString(),
                SearchApiConst.ORIGIN_FILTER to SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_SORT_PAGE
        )
        val expectedSelectedFilterMap = mapOf(
                SearchApiConst.PMIN to minPriceValue.toString()
        )
        val expectedSelectedSortMap = mapOf(clickedSortWithoutApplyFilter.sort.key to clickedSortWithoutApplyFilter.sort.value)
        val expectedSelectedSortName = clickedSortWithoutApplyFilter.sort.name

        `Then assert apply button parameters is expected`(
                expectedMapParameter, expectedSelectedFilterMap, expectedSelectedSortMap, expectedSelectedSortName
        )
    }
}