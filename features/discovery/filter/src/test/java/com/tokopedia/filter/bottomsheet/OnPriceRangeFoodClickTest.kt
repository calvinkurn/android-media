package com.tokopedia.filter.bottomsheet

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.pricerangecheckbox.PriceRangeFilterItemUiModel
import com.tokopedia.filter.bottomsheet.pricerangecheckbox.PriceRangeFilterUiModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.filter.testutils.jsonToObject
import org.junit.Test

internal class OnPriceRangeFoodClickTest: SortFilterBottomSheetViewModelTestFixtures() {

    @Test
    fun `onPricerangeFoodClicked with given PriceRangeFilterItemUiModel to apply filter`() {
        val existingMapParameter = createMapParameter()
        val dynamicFilterModel = "dynamic-filter-model-price-range.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(existingMapParameter, dynamicFilterModel)

        val sortFilterList = this.sortFilterList!!
        val selectedFilter = dynamicFilterModel.data.filter[0] // Just choose any filter
        val priceRangeUiModel = sortFilterList.findPriceRangeFilterUiModel(selectedFilter)
            ?: throw AssertionError("Cannot find selected filter ${selectedFilter.title} in Sort Filter List")
        val clickedOptionViewModel = priceRangeUiModel.priceRangeList.findLast {
            !it.isSelected
        }!! // Just choose any un-selected option

        `When an Checkbox Option is Clicked And Applied`(clickedOptionViewModel, true)

        val selectedOptionViewModel = getSelectedRangeFilterOption(priceRangeUiModel, clickedOptionViewModel)
        `Then assert rangeFilterItem selected state`(selectedOptionViewModel, true)
        `Then assert sort filter view is updated`(sortFilterList.indexOf(priceRangeUiModel))
        `Then assert map parameter values contains the clicked option`(existingMapParameter, selectedOptionViewModel)
        `Then assert map parameter contains origin_filter=filter`()
        `Then assert ACTIVE filter map parameter contains the clicked Price Range option`(selectedOptionViewModel)
    }

    private fun `When an Checkbox Option is Clicked And Applied`(priceRangeFilterItemUiModel: PriceRangeFilterItemUiModel,
                                                                 isSelected: Boolean) {
        sortFilterBottomSheetViewModel.onPriceRangeFoodClick(priceRangeFilterItemUiModel, isSelected)
        sortFilterBottomSheetViewModel.applySortFilter()
    }

    private fun `Then assert rangeFilterItem selected state`(priceRangeFilterItemUiModel: PriceRangeFilterItemUiModel, expectedIsSelected: Boolean) {
        assert(priceRangeFilterItemUiModel.option.inputState.toBoolean() == expectedIsSelected) {
            "Option ${priceRangeFilterItemUiModel.option.name} inputState should be $expectedIsSelected"
        }

        assert(priceRangeFilterItemUiModel.isSelected == expectedIsSelected) {
            "Option ${priceRangeFilterItemUiModel.option.name} isSelected should be $expectedIsSelected"
        }
    }

    private fun `Then assert map parameter values contains the clicked option`(
        existingMapParameter: Map<String, String>, priceRangeFilterItemUiModel: PriceRangeFilterItemUiModel
    ) {
        val mapParameter = sortFilterBottomSheetViewModel.mapParameter

        assertMapValueContainsClickedOption(mapParameter, priceRangeFilterItemUiModel)
        assertNonClickedParameterShouldNotChange(existingMapParameter, mapParameter, priceRangeFilterItemUiModel)
    }

    private fun assertMapValueContainsClickedOption(mapParameter: Map<String, String>, priceRangeFilterItemUiModel: PriceRangeFilterItemUiModel) {
        val option = priceRangeFilterItemUiModel.option

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
        priceRangeFilterItemUiModel: PriceRangeFilterItemUiModel
    ) {
        existingMapParameter.forEach {
            val isNotClickedOption = it.key != priceRangeFilterItemUiModel.option.key

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
        clickedPriceRangeFilterItemUiModel: PriceRangeFilterItemUiModel
    ) {
        val activeFilterMapParameter = sortFilterBottomSheetViewModel.getSelectedFilterMap()
        assertMapValueContainsClickedOption(activeFilterMapParameter, clickedPriceRangeFilterItemUiModel)
    }


    private fun getSelectedRangeFilterOption(
        priceRangeFilterUiModel: PriceRangeFilterUiModel,
        priceRangeFilterItemUiModel: PriceRangeFilterItemUiModel
    ) : PriceRangeFilterItemUiModel {
        return priceRangeFilterUiModel.priceRangeList.first { optionVM ->
            optionVM.option == priceRangeFilterItemUiModel.option
        }
    }
}