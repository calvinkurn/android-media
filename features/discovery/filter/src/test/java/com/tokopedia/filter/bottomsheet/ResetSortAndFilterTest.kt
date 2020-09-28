package com.tokopedia.filter.bottomsheet

import com.tokopedia.discovery.common.EventObserver
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.filter.FilterViewModel
import com.tokopedia.filter.bottomsheet.pricefilter.PriceFilterViewModel
import com.tokopedia.filter.bottomsheet.sort.SortViewModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper.OPTION_SEPARATOR
import com.tokopedia.filter.testutils.jsonToObject
import org.junit.Test

internal class ResetSortAndFilterTest: SortFilterBottomSheetViewModelTestFixtures() {

    private val updatedViewPositionList = mutableListOf<Int>()
    private val updateViewPositionListObserver = EventObserver<Int> {
        updatedViewPositionList.add(it)
    }

    private fun observeUpdateViewInPositionLiveData() {
        sortFilterBottomSheetViewModel.updateViewInPositionEventLiveData.observeForever(updateViewPositionListObserver)
    }

    private fun unObserveUpdateViewInPositionLiveData() {
        sortFilterBottomSheetViewModel.updateViewInPositionEventLiveData.removeObserver(updateViewPositionListObserver)
    }

    @Test
    fun `Reset without active filter or sort should not do anything (edge cases)`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        `When reset sort and filter and applied`()

        `Then assert no update view in position`()
        `Then assert map parameter is as expected`(mapOf(SearchApiConst.OB to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT))
        `Then assert button reset visibility`(false)
        `Then assert filter view is expanded`()
    }

    private fun `When reset sort and filter and applied`() {
        sortFilterBottomSheetViewModel.resetSortAndFilter()
        sortFilterBottomSheetViewModel.applySortFilter()
    }

    private fun `Then assert no update view in position`() {
        assert(sortFilterBottomSheetViewModel.updateViewInPositionEventLiveData.value == null) {
            "Update view in position should be null"
        }
    }

    @Test
    fun `Reset should remove all filters`() {
        observeUpdateViewInPositionLiveData()

        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val mapParameter = createMapWithVariousFilters(dynamicFilterModel)
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        `When reset sort and filter and applied`()

        // Position 0 is sort, 1 is filter location, 3 is filter price, 5 is filter toko
        `Then assert reset functionality`(listOf(1, 3, 5), true)

        unObserveUpdateViewInPositionLiveData()
    }

    private fun createMapWithVariousFilters(dynamicFilterModel: DynamicFilterModel): Map<String, String> {
        val locationFilter = dynamicFilterModel.data.filter[0]
        val priceFilter = dynamicFilterModel.data.filter[2]
        val selectedPriceRange = priceFilter.options.find { it.key == Option.KEY_PRICE_RANGE_1 }!!
        val minPriceFilterValue = selectedPriceRange.valMin
        val maxPriceFilterValue = selectedPriceRange.valMax

        return mutableMapOf<String, String>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.OFFICIAL] = true.toString()
            it[SearchApiConst.FCITY] = locationFilter.options[0].value + OPTION_SEPARATOR + locationFilter.options[2].value
            it[SearchApiConst.PMIN] = minPriceFilterValue
            it[SearchApiConst.PMAX] = maxPriceFilterValue
            it[SearchApiConst.OB] = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
        }
    }

    private fun `Then assert reset functionality`(updatedViewPositionList: List<Int>, expectedIsApplyButtonVisible: Boolean) {
        `Then assert sort filter view is reset`()
        `Then assert updated view position`(updatedViewPositionList)
        `Then assert button reset visibility`(false)
        `Then assert button apply visibility`(expectedIsApplyButtonVisible)
        `Then assert map parameter is as expected`(mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OB to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT))
        `Then assert selected filter map is as expected`(emptyMap())
        `Then assert selected sort map is as expected`(emptyMap())
        `Then assert selected sort name`("")
        `Then assert filter view is expanded`()
    }

    private fun `Then assert sort filter view is reset`() {
        this.sortFilterList!!.forEach {
            when (it) {
                is SortViewModel -> it.assertSortIsReset()
                is FilterViewModel -> it.assertFilterIsReset()
                is PriceFilterViewModel -> it.assertPriceFilterIsReset()
            }
        }
    }

    private fun SortViewModel.assertSortIsReset() {
        sortItemViewModelList.forEach {
            val expectedIsSelected = it.sort.value == SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT
            assert(it.isSelected == expectedIsSelected) {
                "Sort item ${it.sort.name} is selected should be $expectedIsSelected."
            }
        }
    }

    private fun FilterViewModel.assertFilterIsReset() {
        optionViewModelList.forEach {
            assert(!it.isSelected) {
                "Option ${it.option.name} is selected should be false"
            }
            assert(!(it.option.inputState.toBoolean())) {
                "Option ${it.option.name} inputState should be false"
            }
        }
    }

    private fun PriceFilterViewModel.assertPriceFilterIsReset() {
        assert(minPriceFilterValue == "") {
            "Min Price Filter Value is ${minPriceFilterValue}, expected is empty string"
        }

        assert(maxPriceFilterValue == "") {
            "Max Price Filter Value is ${maxPriceFilterValue}, expected is empty string"
        }

        priceRangeOptionViewModelList.forEach {
            assert(!it.isSelected) {
                "Price Range Option ${it.option.name} is selected should be false"
            }
        }
    }

    private fun `Then assert updated view position`(expectedUpdatedViewPositionList: List<Int>) {
        assert(updatedViewPositionList == expectedUpdatedViewPositionList) {
            "Updated View Position list is $updatedViewPositionList. Expected is: $expectedUpdatedViewPositionList"
        }
    }

    @Test
    fun `Reset should set sort parameter as default sort`() {
        observeUpdateViewInPositionLiveData()

        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val sortItem = dynamicFilterModel.data.sort.find { it.value != SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT }!!
        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OB to sortItem.value)
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        `When reset sort and filter and applied`()

        `Then assert reset functionality`(listOf(0), true)

        unObserveUpdateViewInPositionLiveData()
    }

    @Test
    fun `Reset after sort with apply filter - should remove all filters and set default sort`() {
        observeUpdateViewInPositionLiveData()

        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OB to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val clickedSort = this.sortFilterList!!.getUnselectedSortWithApplyFilter()
        sortFilterBottomSheetViewModel.onSortItemClick(clickedSort)
        sortFilterBottomSheetViewModel.resetSortAndFilter()

        // View position 0 is updated twice: onSortItemClick and resetSortAndFilter
        `Then assert reset functionality`(listOf(0, 0), false)

        unObserveUpdateViewInPositionLiveData()
    }

    @Test
    fun `Reset after apply filter - should remove all filters and set default sort`() {
        observeUpdateViewInPositionLiveData()

        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OB to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val filterViewModel = this.sortFilterList!!.findFilterViewModel(dynamicFilterModel.data.filter[0])!!
        val clickedOptionViewModel = filterViewModel.getAnyUnselectedFilter()
        sortFilterBottomSheetViewModel.onOptionClick(filterViewModel, clickedOptionViewModel)
        sortFilterBottomSheetViewModel.resetSortAndFilter()

        // View position 1 is updated twice: onOptionClick and resetSortAndFilter
        `Then assert reset functionality`(listOf(1, 1), false)

        unObserveUpdateViewInPositionLiveData()
    }

    @Test
    fun `Reset after apply price range filter - should remove all filters and set default sort`() {
        observeUpdateViewInPositionLiveData()

        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OB to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val priceFilterViewModel = this.sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        val clickedPriceRangeOption = priceFilterViewModel.getUnselectedPriceRangeOption()
        sortFilterBottomSheetViewModel.onPriceRangeOptionClick(priceFilterViewModel, clickedPriceRangeOption)
        sortFilterBottomSheetViewModel.resetSortAndFilter()

        val priceFilterIndex = this.sortFilterList!!.indexOfFirst { it == priceFilterViewModel }
        // View in position priceFilterIndex is updated twice: onPriceRangeOptionClick and resetSortAndFilter
        `Then assert reset functionality`(listOf(priceFilterIndex, priceFilterIndex), false)

        unObserveUpdateViewInPositionLiveData()
    }

    @Test
    fun `Reset after apply minimum price filter text box - should remove all filters and set default sort`() {
        observeUpdateViewInPositionLiveData()

        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OB to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val priceFilterViewModel = this.sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        sortFilterBottomSheetViewModel.onMinPriceFilterEdited(priceFilterViewModel, 10_000)
        sortFilterBottomSheetViewModel.resetSortAndFilter()

        val priceFilterIndex = this.sortFilterList!!.indexOfFirst { it == priceFilterViewModel }
        `Then assert reset functionality`(listOf(priceFilterIndex), false)

        unObserveUpdateViewInPositionLiveData()
    }

    @Test
    fun `Reset after apply maximum price filter text box - should remove all filters and set default sort`() {
        observeUpdateViewInPositionLiveData()

        val mapParameter = mapOf(SearchApiConst.Q to "samsung", SearchApiConst.OB to SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_SORT)
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val priceFilterViewModel = this.sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!
        sortFilterBottomSheetViewModel.onMaxPriceFilterEdited(priceFilterViewModel, 500_000)
        sortFilterBottomSheetViewModel.resetSortAndFilter()

        val priceFilterIndex = this.sortFilterList!!.indexOfFirst { it == priceFilterViewModel }
        `Then assert reset functionality`(listOf(priceFilterIndex), false)

        unObserveUpdateViewInPositionLiveData()
    }
}