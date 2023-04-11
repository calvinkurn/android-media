package com.tokopedia.filter.bottomsheet

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.filter.FilterViewModel
import com.tokopedia.filter.bottomsheet.filter.OptionViewModel
import com.tokopedia.filter.bottomsheet.filter.pricerangecheckbox.PriceRangeFilterCheckboxDataView
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.filter.testutils.jsonToObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class OnOptionClickTest: SortFilterBottomSheetViewModelTestFixtures() {

    @Test
    fun `onOptionClicked with given OptionViewModel to apply filter`() {
        val existingMapParameter = createMapParameter()
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(existingMapParameter, dynamicFilterModel)

        val sortFilterList = this.sortFilterList!!
        val selectedFilter = dynamicFilterModel.data.filter[0] // Just choose any filter
        val filterViewModel = sortFilterList.findFilterViewModel(selectedFilter)
                ?: throw AssertionError("Cannot find selected filter ${selectedFilter.title} in Sort Filter List")
        val clickedOptionViewModel = filterViewModel.optionViewModelList.findLast {
            !it.isSelected
        }!! // Just choose any un-selected option

        `When an Option is Clicked And Applied`(filterViewModel, clickedOptionViewModel)

        val selectedOptionViewModel = getSelectedOptionViewModel(filterViewModel, clickedOptionViewModel)
        `Then assert option selected state`(selectedOptionViewModel, true)
        `Then assert sort filter view is updated`(sortFilterList.indexOf(filterViewModel))
        `Then assert map parameter values contains the clicked option`(existingMapParameter, selectedOptionViewModel)
        `Then assert map parameter contains origin_filter=filter`()
        `Then assert ACTIVE filter map parameter contains the clicked option`(selectedOptionViewModel)
    }

    private fun getSelectedOptionViewModel(
        filterRefreshable: FilterRefreshable,
        clickedOptionViewModel: OptionViewModel
    ) : OptionViewModel {
        return filterRefreshable.optionViewModelList.first { optionVM ->
            optionVM.option == clickedOptionViewModel.option
        }
    }

    private fun `When an Option is Clicked And Applied`(filterRefreshable: FilterRefreshable, clickedOptionViewModel: OptionViewModel) {
        sortFilterBottomSheetViewModel.onOptionClick(filterRefreshable, clickedOptionViewModel)
        sortFilterBottomSheetViewModel.applySortFilter()
    }

    private fun `Then assert map parameter values contains the clicked option`(
            existingMapParameter: Map<String, String>, clickedOptionViewModel: OptionViewModel
    ) {
        val mapParameter = sortFilterBottomSheetViewModel.mapParameter

        assertMapValueContainsClickedOption(mapParameter, clickedOptionViewModel)
        assertNonClickedParameterShouldNotChange(existingMapParameter, mapParameter, clickedOptionViewModel)
    }

    private fun assertMapValueContainsClickedOption(mapParameter: Map<String, String>, clickedOptionViewModel: OptionViewModel) {
        val option = clickedOptionViewModel.option

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
            clickedOptionViewModel: OptionViewModel
    ) {
        existingMapParameter.forEach {
            val isNotClickedOption = it.key != clickedOptionViewModel.option.key

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

    private fun `Then assert ACTIVE filter map parameter contains the clicked option`(
            clickedOptionViewModel: OptionViewModel
    ) {
        val activeFilterMapParameter = sortFilterBottomSheetViewModel.getSelectedFilterMap()
        assertMapValueContainsClickedOption(activeFilterMapParameter, clickedOptionViewModel)
    }

    @Test
    fun `onOptionClicked with given OptionViewModel to un-apply filter`() {
        val existingMapParameter = createMapParameter()
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(existingMapParameter, dynamicFilterModel)

        val sortFilterList = this.sortFilterList!!
        val selectedFilter = dynamicFilterModel.data.filter.find { it.title == "Toko" }!! // Un-apply Official Store filter
        val selectedOption = OptionHelper.generateOptionFromUniqueId(
                OptionHelper.constructUniqueId(SearchApiConst.OFFICIAL, true.toString(), "Official Store")
        )
        val filterViewModel = sortFilterList.findFilterViewModel(selectedFilter)
                ?: throw AssertionError("Cannot find selected filter ${selectedFilter.title} in Sort Filter List")
        val clickedOptionViewModel = filterViewModel.optionViewModelList.find {
            it.option.uniqueId == selectedOption.uniqueId
        }!!

        `When an Option is Clicked And Applied`(filterViewModel, clickedOptionViewModel)

        val selectedOptionViewModel = getSelectedOptionViewModel(filterViewModel, clickedOptionViewModel)
        `Then assert option selected state`(selectedOptionViewModel, false)
        `Then assert sort filter view is updated`(sortFilterList.indexOf(filterViewModel))
        `Then assert map parameter values does NOT contain the clicked option`(existingMapParameter, selectedOptionViewModel)
        `Then assert map parameter contains origin_filter=filter`()
        `Then assert ACTIVE filter map parameter does NOT contain the clicked option`(selectedOptionViewModel)
    }

    private fun `Then assert map parameter values does NOT contain the clicked option`(
            existingMapParameter: Map<String, String>, clickedOptionViewModel: OptionViewModel
    ) {
        val mapParameter = sortFilterBottomSheetViewModel.mapParameter

        assertMapValueDoesNotContainClickedOption(mapParameter, clickedOptionViewModel)
        assertNonClickedParameterShouldNotChange(existingMapParameter, mapParameter, clickedOptionViewModel)
    }

    private fun assertMapValueDoesNotContainClickedOption(mapParameter: Map<String, String>, clickedOptionViewModel: OptionViewModel) {
        val option = clickedOptionViewModel.option

        val mapParameterOptionValues = mapParameter[option.key]?.split(",") ?: listOf()
        val optionValues = option.value.split(",")

        assert(!mapParameterOptionValues.containsAll(optionValues)) {
            "Map Parameter ${option.key} should NOT contains all value $optionValues.\nActual Map Parameter contains $mapParameterOptionValues"
        }
    }

    private fun `Then assert ACTIVE filter map parameter does NOT contain the clicked option`(
            clickedOptionViewModel: OptionViewModel
    ) {
        val activeFilterMapParameter = sortFilterBottomSheetViewModel.getSelectedFilterMap()
        assertMapValueDoesNotContainClickedOption(activeFilterMapParameter, clickedOptionViewModel)
    }

    @Test
    fun `onOptionClicked with given OptionViewModel to apply CATEGORY filter`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val selectedFilter = dynamicFilterModel.getCategoryFilter()
        val existingMapParameter = createMapParameterWithCategoryFilter(selectedFilter.options[0])
        `Given SortFilterBottomSheet view is already created`(existingMapParameter, dynamicFilterModel)

        val sortFilterList = this.sortFilterList!!
        val filterViewModel = sortFilterList.findFilterViewModel(selectedFilter)
                ?: throw AssertionError("Cannot find selected filter ${selectedFilter.title} in Sort Filter List")
        val clickedOptionViewModel = filterViewModel.optionViewModelList.findLast {
            !it.isSelected
        }!! // Just choose any un-selected option

        `When an Option is Clicked And Applied`(filterViewModel, clickedOptionViewModel)

        val selectedOptionViewModel = getSelectedOptionViewModel(filterViewModel, clickedOptionViewModel)
        `Then assert ONLY one OptionViewModel isSelected`(filterViewModel)
        `Then assert option selected state`(selectedOptionViewModel, true)
        `Then assert sort filter view is updated`(sortFilterList.indexOf(filterViewModel))
        `Then assert map parameter values ONLY contains the clicked option`(existingMapParameter, selectedOptionViewModel)
        `Then assert map parameter contains origin_filter=filter`()
        `Then assert ACTIVE filter map parameter ONLY contains the clicked option`(selectedOptionViewModel)
    }

    private fun createMapParameterWithCategoryFilter(categoryOption: Option): Map<String, String> {
        return mutableMapOf<String, String>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.OFFICIAL] = true.toString() // Filter Official Store
            it[SearchApiConst.OB] = 23.toString() // Sorted by Best Match
            it[categoryOption.key] = categoryOption.value
        }
    }

    private fun `Then assert ONLY one OptionViewModel isSelected`(filterViewModel: FilterViewModel) {
        val selectedOptionCount = filterViewModel.optionViewModelList.filter { it.isSelected }.size

        assert(selectedOptionCount == 1) {
            "Filter ${filterViewModel.filter.title} should only have 1 selected option. Actual selected option count: $selectedOptionCount"
        }
    }

    private fun `Then assert map parameter values ONLY contains the clicked option`(existingMapParameter: Map<String, String>, clickedOptionViewModel: OptionViewModel) {
        val mapParameter = sortFilterBottomSheetViewModel.mapParameter

        assertMapValueOnlyContainsClickedOption(mapParameter, clickedOptionViewModel)
        assertNonClickedParameterShouldNotChange(existingMapParameter, mapParameter, clickedOptionViewModel)
    }

    private fun assertMapValueOnlyContainsClickedOption(mapParameter: Map<String, String>, clickedOptionViewModel: OptionViewModel) {
        val option = clickedOptionViewModel.option

        assert(mapParameter[option.key] == option.value) {
            "Map Parameter \"${option.key}\" should ONLY contains \"${option.value}\".\n" +
                    "Actual Map Parameter contains \"${mapParameter[option.key]}\""
        }
    }

    private fun `Then assert ACTIVE filter map parameter ONLY contains the clicked option`(
            clickedOptionViewModel: OptionViewModel
    ) {
        val activeFilterMapParameter = sortFilterBottomSheetViewModel.getSelectedFilterMap()
        assertMapValueOnlyContainsClickedOption(activeFilterMapParameter, clickedOptionViewModel)
    }

    @Test
    fun `onOptionClicked with given OptionViewModel to un-apply CATEGORY filter`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val selectedFilter = dynamicFilterModel.getCategoryFilter()
        val existingMapParameter = createMapParameterWithCategoryFilter(selectedFilter.options[0])
        `Given SortFilterBottomSheet view is already created`(existingMapParameter, dynamicFilterModel)

        val sortFilterList = this.sortFilterList!!
        val filterViewModel = sortFilterList.findFilterViewModel(selectedFilter)
                ?: throw AssertionError("Cannot find selected filter ${selectedFilter.title} in Sort Filter List")
        val clickedOptionViewModel = filterViewModel.optionViewModelList[0]

        `When an Option is Clicked And Applied`(filterViewModel, clickedOptionViewModel)

        val selectedOptionViewModel = getSelectedOptionViewModel(filterViewModel, clickedOptionViewModel)
        `Then assert option selected state`(selectedOptionViewModel, false)
        `Then assert sort filter view is updated`(sortFilterList.indexOf(filterViewModel))
        `Then assert map parameter values does NOT contain the clicked option`(existingMapParameter, selectedOptionViewModel)
        `Then assert map parameter contains origin_filter=filter`()
        `Then assert ACTIVE filter map parameter does NOT contain the clicked option`(selectedOptionViewModel)
    }

    @Test
    fun `onOptionClicked with given OptionViewModel to apply RADIO TYPE filter`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val selectedFilter = dynamicFilterModel.getRadioTypeFilter()
        val existingMapParameter = createMapParameterWithRadioTypeFilter(selectedFilter.options[0])
        `Given SortFilterBottomSheet view is already created`(existingMapParameter, dynamicFilterModel)

        val sortFilterList = this.sortFilterList!!
        val filterViewModel = sortFilterList.findFilterViewModel(selectedFilter)
                ?: throw AssertionError("Cannot find selected filter ${selectedFilter.title} in Sort Filter List")
        val clickedOptionViewModel = filterViewModel.optionViewModelList.findLast {
            !it.isSelected
        }!! // Just choose any un-selected option

        `When an Option is Clicked And Applied`(filterViewModel, clickedOptionViewModel)

        val selectedOptionViewModel = getSelectedOptionViewModel(filterViewModel, clickedOptionViewModel)
        `Then assert ONLY one OptionViewModel isSelected`(filterViewModel)
        `Then assert option selected state`(selectedOptionViewModel, true)
        `Then assert sort filter view is updated`(sortFilterList.indexOf(filterViewModel))
        `Then assert map parameter values ONLY contains the clicked option`(existingMapParameter, selectedOptionViewModel)
        `Then assert map parameter contains origin_filter=filter`()
        `Then assert ACTIVE filter map parameter ONLY contains the clicked option`(selectedOptionViewModel)
    }

    private fun DynamicFilterModel.getRadioTypeFilter(): Filter {
        return this.data.filter.find { filter ->
            !filter.isPriceFilter && filter.options.any { option -> option.isTypeRadio }
        }!!
    }

    private fun createMapParameterWithRadioTypeFilter(radioTypeOption: Option): Map<String, String> {
        return mutableMapOf<String, String>().also {
            it[SearchApiConst.Q] = "samsung"
            it[SearchApiConst.OFFICIAL] = true.toString() // Filter Official Store
            it[SearchApiConst.OB] = 23.toString() // Sorted by Best Match
            it[radioTypeOption.key] = radioTypeOption.value
        }
    }

    @Test
    fun `onOptionClicked with given OptionViewModel to un-apply RADIO TYPE filter`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val selectedFilter = dynamicFilterModel.getRadioTypeFilter()
        val existingMapParameter = createMapParameterWithRadioTypeFilter(selectedFilter.options[0])
        `Given SortFilterBottomSheet view is already created`(existingMapParameter, dynamicFilterModel)

        val sortFilterList = this.sortFilterList!!
        val filterViewModel = sortFilterList.findFilterViewModel(selectedFilter)
                ?: throw AssertionError("Cannot find selected filter ${selectedFilter.title} in Sort Filter List")
        val clickedOptionViewModel = filterViewModel.optionViewModelList[0]

        `When an Option is Clicked And Applied`(filterViewModel, clickedOptionViewModel)

        val selectedOptionViewModel = getSelectedOptionViewModel(filterViewModel, clickedOptionViewModel)
        `Then assert option selected state`(selectedOptionViewModel, false)
        `Then assert sort filter view is updated`(sortFilterList.indexOf(filterViewModel))
        `Then assert map parameter values does NOT contain the clicked option`(existingMapParameter, selectedOptionViewModel)
        `Then assert map parameter contains origin_filter=filter`()
        `Then assert ACTIVE filter map parameter does NOT contain the clicked option`(selectedOptionViewModel)
    }

    @Test
    fun `onOptionClicked with given OptionViewModel to apply filter - should show apply button and reset`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        val sortFilterList = this.sortFilterList!!
        val selectedFilter = dynamicFilterModel.data.filter[0] // Just choose any filter
        val filterViewModel = sortFilterList.findFilterViewModel(selectedFilter)
                ?: throw AssertionError("Cannot find selected filter ${selectedFilter.title} in Sort Filter List")
        val clickedOptionViewModel = filterViewModel.optionViewModelList.findLast {
            !it.isSelected
        }!! // Just choose any un-selected option

        `When an Option is Clicked And Applied`(filterViewModel, clickedOptionViewModel)

        `Then assert button apply is shown with loading`()
        `Then assert button reset visibility`(true)
    }

    @Test
    fun `onOptionClicked with given OptionViewModel that will remove all filter - should not show reset and show apply button`() {
        val mapParameter = mutableMapOf<String, String>().also {
            it[SearchApiConst.OFFICIAL] = true.toString()
        }
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapParameter, dynamicFilterModel)

        val sortFilterList = this.sortFilterList!!
        val selectedFilter = dynamicFilterModel.data.filter.find { it.title == "Toko" }!! // Un-apply Official Store filter
        val selectedOption = OptionHelper.generateOptionFromUniqueId(
                OptionHelper.constructUniqueId(SearchApiConst.OFFICIAL, true.toString(), "Official Store")
        )
        val filterViewModel = sortFilterList.findFilterViewModel(selectedFilter)
                ?: throw AssertionError("Cannot find selected filter ${selectedFilter.title} in Sort Filter List")
        val clickedOptionViewModel = filterViewModel.optionViewModelList.find {
            it.option.uniqueId == selectedOption.uniqueId
        }!!

        `When an Option is Clicked And Applied`(filterViewModel, clickedOptionViewModel)

        `Then assert button apply is shown with loading`()
        `Then assert button reset visibility`(false)
    }

    @Test
    fun `onOptionClicked will always expand the filter view`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        val filterViewModel = this.sortFilterList!!.findFilterViewModel(dynamicFilterModel.data.filter[0])!!
        val clickedOptionViewModel = filterViewModel.getAnyUnselectedFilter()
        sortFilterBottomSheetViewModel.onOptionClick(filterViewModel, clickedOptionViewModel)

        `Then assert filter view is expanded`()
    }

    @Test
    fun `onOptionClick on the same option twice should not show apply button`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        val filterViewModel = this.sortFilterList!!.findFilterViewModel(dynamicFilterModel.data.filter[0])!!
        val clickedOptionViewModel = filterViewModel.getAnyUnselectedFilter()
        sortFilterBottomSheetViewModel.onOptionClick(filterViewModel, clickedOptionViewModel)
        val selectedOptionViewModel = getSelectedOptionViewModel(filterViewModel, clickedOptionViewModel)
        sortFilterBottomSheetViewModel.onOptionClick(filterViewModel, selectedOptionViewModel)

        `Then assert button apply is not shown`()
    }

    @Test
    fun `onOptionClick will also select option on other filter sections`() {
        val dynamicFilterModel = "dynamic-filter-model-offering.json".jsonToObject<DynamicFilterModel>()
        val selectedFilter = dynamicFilterModel.data.filter.find { it.title == "Bebas Ongkir" }!!
        `Given SortFilterBottomSheet view is already created`(mapOf(), dynamicFilterModel)

        val filterViewModel = sortFilterList!!.findFilterViewModel(selectedFilter)
        val clickedOptionViewModel = filterViewModel!!.optionViewModelList[0]
        `When an Option is Clicked And Applied`(filterViewModel, clickedOptionViewModel)

        `Then assert the option is selected in other Filter section`(clickedOptionViewModel.option)
    }

    private fun `Then assert the option is selected in other Filter section`(clickedOption: Option) {
        val allOptionViewModelList = sortFilterList!!
            .filterIsInstance<FilterViewModel>()
            .filter { filterViewModel -> clickedOption in filterViewModel.filter.options }
            .map { filterViewModel ->
                filterViewModel.optionViewModelList.first { it.option == clickedOption }
            }
        assertTrue(allOptionViewModelList.isNotEmpty())
        val allOptionViewModelListIsSelected = allOptionViewModelList
            .all { it.isSelected }
        assertTrue(allOptionViewModelListIsSelected)
    }

    @Test
    fun `onOptionClicked with given PriceRangeFilterCheckboxDataView to apply filter`() {
        val existingMapParameter = createMapParameter()
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val templatePricingFood = "template_pricing_food"
        `Given SortFilterBottomSheet view is already created`(existingMapParameter, dynamicFilterModel)

        val sortFilterList = this.sortFilterList!!
        val selectedFilter = dynamicFilterModel.data.filter.first { it.templateName == templatePricingFood } // Just choose any filter
        val priceRangeFilter = sortFilterList.findPriceRangeFilterCheckboxDataView(selectedFilter)
            ?: throw AssertionError("Cannot find selected filter ${selectedFilter.title} in Sort Filter List")
        val clickedOptionViewModel = priceRangeFilter.optionViewModelList.first { !it.isSelected } // Just choose any un-selected option

        `When an Option is Clicked And Applied`(priceRangeFilter, clickedOptionViewModel)

        val priceRangeFilterCheckboxDataView = this.sortFilterList!!.filterIsInstance<PriceRangeFilterCheckboxDataView>().first()
        val optionViewModels = selectedFilter.options.take(priceRangeFilterCheckboxDataView.optionViewModelList.size).filterIndexed { index, item ->
            priceRangeFilterCheckboxDataView.optionViewModelList[index].option.description == item.description
        }

        `Then assert option list doesn't sort by selected or name`(optionViewModels)
    }

    private fun `Then assert option list doesn't sort by selected or name`(
        options: List<Option>
    ) {
        this.sortFilterList?.filterIsInstance<PriceRangeFilterCheckboxDataView>()?.first().let { visitable ->
            assertEquals(visitable?.optionViewModelList?.size, options.size)
            visitable?.optionViewModelList?.forEachIndexed { index, item ->
                val option = options[index]
                assertEquals(option.name, item.option.name)
                assertEquals(option.description, item.option.description)
                assertEquals(option.key, item.option.key)
                assertEquals(option.value, item.option.value)
                assertEquals(option.inputType, item.option.inputType)
            }
        }
    }
}