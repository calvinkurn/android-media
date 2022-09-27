package com.tokopedia.filter.bottomsheet

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.filter.OptionViewModel
import com.tokopedia.filter.bottomsheet.filter.pricerangecheckbox.PriceRangeFilterCheckboxDataView
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.filter.testutils.jsonToObject
import org.junit.Test

internal class OnPriceRangeFoodClickTest: SortFilterBottomSheetViewModelTestFixtures() {

    @Test
    fun `onPricerangeFoodClicked with given PriceRangeFilterCheckboxDataView to apply filter`() {
        val existingMapParameter = createMapParameter()
        val dynamicFilterModel = "dynamic-filter-model-price-range.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(existingMapParameter, dynamicFilterModel)

        val sortFilterList = this.sortFilterList!!
        val selectedFilter = dynamicFilterModel.data.filter[0] // Just choose any filter
        val priceRangeFilterCheckboxDataView = sortFilterList.findPriceRangeFilterCheckboxDataView(selectedFilter)
            ?: throw AssertionError("Cannot find selected filter ${selectedFilter.title} in Sort Filter List")
        val clickedOptionViewModel = priceRangeFilterCheckboxDataView.optionViewModelList.findLast {
            !it.isSelected
        }!! // Just choose any un-selected option

        `When an Checkbox Option is Clicked And Applied`(priceRangeFilterCheckboxDataView, clickedOptionViewModel)

        val selectedOptionViewModel = getSelectedRangeFilterOption(priceRangeFilterCheckboxDataView, clickedOptionViewModel)
        `Then assert rangeFilterItem selected state`(selectedOptionViewModel, true)
        `Then assert sort filter view is updated`(sortFilterList.indexOf(priceRangeFilterCheckboxDataView))
        `Then assert map parameter values contains the clicked option`(existingMapParameter, selectedOptionViewModel)
        `Then assert map parameter contains origin_filter=filter`()
        `Then assert ACTIVE filter map parameter contains the clicked Price Range option`(selectedOptionViewModel)
    }

    private fun `When an Checkbox Option is Clicked And Applied`(filterRefreshable: FilterRefreshable,
                                                                 optionViewModel: OptionViewModel) {
        sortFilterBottomSheetViewModel.onOptionClick(filterRefreshable, optionViewModel)
        sortFilterBottomSheetViewModel.applySortFilter()
    }

    private fun `Then assert rangeFilterItem selected state`(optionViewModel: OptionViewModel, expectedIsSelected: Boolean) {
        assert(optionViewModel.option.inputState.toBoolean() == expectedIsSelected) {
            "Option ${optionViewModel.option.name} inputState should be $expectedIsSelected"
        }

        assert(optionViewModel.isSelected == expectedIsSelected) {
            "Option ${optionViewModel.option.name} isSelected should be $expectedIsSelected"
        }
    }

    private fun `Then assert map parameter values contains the clicked option`(
        existingMapParameter: Map<String, String>,
        optionViewModel: OptionViewModel
    ) {
        val mapParameter = sortFilterBottomSheetViewModel.mapParameter

        assertMapValueContainsClickedOption(mapParameter, optionViewModel)
        assertNonClickedParameterShouldNotChange(existingMapParameter, mapParameter, optionViewModel)
    }

    private fun assertMapValueContainsClickedOption(mapParameter: Map<String, String>,
                                                    optionViewModel: OptionViewModel) {
        val option = optionViewModel.option

        val mapParameterOptions = mapParameter[option.key]?.split(OptionHelper.OPTION_SEPARATOR) ?: listOf()
        val optionValues = option.value.split(OptionHelper.VALUE_SEPARATOR)

        var mapParameterContainsClickedOption = false
        mapParameterOptions.forEach { mapParameterOption ->
            val mapParameterOptionValues = mapParameterOption.split(OptionHelper.VALUE_SEPARATOR)

            mapParameterContainsClickedOption =
                mapParameterOptionValues.size == optionValues.size && mapParameterOptionValues.containsAll(optionValues)

            if (mapParameterContainsClickedOption) return
        }

        assert(mapParameterContainsClickedOption) {
            "Map Parameter ${option.key} should contains all value $optionValues.\nActual Map Parameter contains $mapParameterOptions"
        }
    }

    private fun assertNonClickedParameterShouldNotChange(
        existingMapParameter: Map<String, String>,
        currentMapParameter: Map<String, String>,
        optionViewModel: OptionViewModel
    ) {
        existingMapParameter.forEach {
            val isNotClickedOption = it.key != optionViewModel.option.key

            if (isNotClickedOption) {
                assert(currentMapParameter[it.key] == it.value) {
                    "Map parameter \"${it.key}\" should contain value \"${it.value}\". Actual is \"${currentMapParameter[it.key]}\"."
                }
            }
        }
    }

    private fun `Then assert map parameter contains origin_filter=filter`() {
        val originFilter = sortFilterBottomSheetViewModel.mapParameter[SearchApiConst.ORIGIN_FILTER]

        assert(originFilter == SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE) {
            "Origin filter should be ${SearchApiConst.DEFAULT_VALUE_OF_ORIGIN_FILTER_FROM_FILTER_PAGE}"
        }
    }

    private fun `Then assert ACTIVE filter map parameter contains the clicked Price Range option`(
        clickedOptionViewModel: OptionViewModel
    ) {
        val activeFilterMapParameter = sortFilterBottomSheetViewModel.getSelectedFilterMap()
        assertMapValueContainsClickedOption(activeFilterMapParameter, clickedOptionViewModel)
    }


    private fun getSelectedRangeFilterOption(
        priceRangeFilterCheckboxDataView: PriceRangeFilterCheckboxDataView,
        optionViewModel: OptionViewModel
    ) : OptionViewModel {
        return priceRangeFilterCheckboxDataView.optionViewModelList.first { optionVM ->
            optionVM.option == optionViewModel.option
        }
    }
}