package com.tokopedia.filter.bottomsheet

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.bottomsheet.SortFilterBottomSheetViewModel.Companion.MAX_OPTION_SIZE
import com.tokopedia.filter.bottomsheet.filter.FilterViewModel
import com.tokopedia.filter.bottomsheet.filter.OptionViewModel
import com.tokopedia.filter.bottomsheet.filter.pricerangecheckbox.PriceRangeFilterCheckboxDataView
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterDataView
import com.tokopedia.filter.bottomsheet.keywordfilter.KeywordFilterItemDataView
import com.tokopedia.filter.bottomsheet.pricefilter.PriceFilterViewModel
import com.tokopedia.filter.bottomsheet.pricefilter.PriceOptionViewModel
import com.tokopedia.filter.bottomsheet.sort.SortItemViewModel
import com.tokopedia.filter.bottomsheet.sort.SortViewModel
import com.tokopedia.filter.common.data.DynamicFilterModel
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.data.Sort
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper.OPTION_SEPARATOR
import com.tokopedia.filter.testutils.jsonToObject
import org.junit.Test

internal class InitializedDynamicFilterViewTest: SortFilterBottomSheetViewModelTestFixtures() {

    @Test
    fun `onViewCreated with null Dynamic Filter Model`() {
        `Given null dynamic filter model`()

        `When view created`()

        `Then assert is loading for dynamic filter`(true)
    }

    private fun `Given null dynamic filter model`() {
        sortFilterBottomSheetViewModel.init(mapOf(), null)
    }

    private fun `When view created`() {
        sortFilterBottomSheetViewModel.onViewCreated()
    }

    private fun `Then assert is loading for dynamic filter`(expectedIsLoading: Boolean) {
        assert(sortFilterBottomSheetViewModel.isLoadingForDynamicFilterLiveData.value == expectedIsLoading) {
            "Is loading for requesting dynamic filter data should be $expectedIsLoading."
        }
    }

    @Test
    fun `onViewCreated without filter list and sort list (edge cases)`() {
        val dynamicFilterModel = "dynamic-filter-model-empty-sort-filter.json".jsonToObject<DynamicFilterModel>()
        `Given initialized SortFilterBottomSheetViewModel`(mapOf(), dynamicFilterModel)

        `When view created`()

        `Then assert sort filter list is null`()
    }

    private fun `Given initialized SortFilterBottomSheetViewModel`(mapParameter: Map<String, String>, dynamicFilterModel: DynamicFilterModel) {
        sortFilterBottomSheetViewModel.init(mapParameter, dynamicFilterModel)
    }

    private fun `Then assert sort filter list is null`() {
        assert(sortFilterList == null) {
            "Sort filter list should be null"
        }
    }

    @Test
    fun `onViewCreated with given Dynamic Filter Model`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `Given initialized SortFilterBottomSheetViewModel`(mapOf(), dynamicFilterModel)

        `When view created`()

        `Then assert button reset visibility`(false)
        `Then assert sort filter list is generated based on dynamic filter model`(dynamicFilterModel)
    }

    private fun `Then assert sort filter list is generated based on dynamic filter model`(dynamicFilterModel: DynamicFilterModel) {
        val sortFilterList: List<Visitable<*>> = this.sortFilterList!!

        sortFilterList.assertSort(dynamicFilterModel)
        sortFilterList.assertFilters(1, dynamicFilterModel)
    }

    private fun List<Visitable<*>>.assertSort(dynamicFilterModel: DynamicFilterModel) {
        val actualFirstIndexVisitable = this[0]
        assert(actualFirstIndexVisitable is SortViewModel) {
            "Sort Filter List first item is ${actualFirstIndexVisitable::class.java.canonicalName}" +
                    ", should be ${SortViewModel::class.java.canonicalName}"
        }

        val sortViewModel = actualFirstIndexVisitable as SortViewModel
        assert(sortViewModel.sortItemViewModelList.size == dynamicFilterModel.data.sort.size) {
            "Sort List size is ${sortViewModel.sortItemViewModelList.size}, should be ${dynamicFilterModel.data.sort.size}"
        }

        sortViewModel.sortItemViewModelList.forEachIndexed { index, it ->
            it.assertSortItemViewModel(dynamicFilterModel.data.sort[index])
        }
    }

    private fun SortItemViewModel.assertSortItemViewModel(expectedSort: Sort) {
        assert(this.sort == expectedSort) {
            "Sort data is ${this.sort}\nExpected $expectedSort."
        }
    }

    private fun List<Visitable<*>>.assertFilters(filterViewModelStartIndex: Int, dynamicFilterModel: DynamicFilterModel) {
        val dynamicFilterModelFilterData = dynamicFilterModel.data.filter

        val actualFilterViewModelSize = this.size - filterViewModelStartIndex
        assert(actualFilterViewModelSize == dynamicFilterModelFilterData.size) {
            "Filter View Model size is $actualFilterViewModelSize, expected: ${dynamicFilterModelFilterData.size}"
        }

        var filterViewModelIndex = filterViewModelStartIndex
        dynamicFilterModelFilterData.forEach { expectedFilter ->
            this[filterViewModelIndex].assertFilterItem(expectedFilter)
            filterViewModelIndex++
        }
    }

    private fun Visitable<*>.assertFilterItem(expectedFilter: Filter) {
        when {
            expectedFilter.isPriceFilter ->
                this.assertAsPriceFilterViewModel(expectedFilter)
            expectedFilter.isKeywordFilter ->
                this.assertAsKeywordFilterDataView(expectedFilter)
            expectedFilter.isPriceRangeCheckboxFilter ->
                this.assertAsPriceRangeCheckboxDataView(expectedFilter)
            else ->
                this.assertAsFilterViewModel(expectedFilter)
        }
    }

    private fun Visitable<*>.assertAsPriceFilterViewModel(expectedFilter: Filter) {
        val priceFilterViewModel = (this as PriceFilterViewModel)
        val actualPriceFilterInPriceFilterViewModel = priceFilterViewModel.priceFilter
        assert(actualPriceFilterInPriceFilterViewModel == expectedFilter) {
            "Price Filter data is $actualPriceFilterInPriceFilterViewModel.\nExpected $expectedFilter"
        }

        priceFilterViewModel.assertMinMaxPriceFilter(expectedFilter)
        priceFilterViewModel.assertPriceRangeFilter(expectedFilter)
    }

    private fun PriceFilterViewModel.assertMinMaxPriceFilter(expectedFilter: Filter) {
        val minPriceOption = expectedFilter.options.find { it.isMinPriceOption }
        val maxPriceOption = expectedFilter.options.find { it.isMaxPriceOption }

        val minPriceOptionName = minPriceOption?.name
        assert(minPriceFilterTitle == minPriceOptionName) {
            "Min Price Filter Title is ${minPriceFilterTitle}, expected is $minPriceOptionName"
        }

        assert(minPriceFilterValue == "") {
            "Min Price Filter Value is ${minPriceFilterValue}, expected is empty string"
        }

        val maxPriceOptionName = maxPriceOption?.name
        assert(maxPriceFilterTitle == maxPriceOptionName) {
            "Max Price Filter Title is ${maxPriceFilterTitle}, expected is $maxPriceOptionName"
        }

        assert(maxPriceFilterValue == "") {
            "Max Price Filter Value is ${maxPriceFilterValue}, expected is empty string"
        }
    }

    private fun PriceFilterViewModel.assertPriceRangeFilter(expectedFilter: Filter) {
        val expectedPriceRangeOption = expectedFilter.options.filter { it.isPriceRange }
        val expectedPriceRangeOptionCount = expectedPriceRangeOption.size

        val priceRangeOptionList = priceRangeOptionViewModelList
        assert(priceRangeOptionList.size == expectedPriceRangeOptionCount) {
            "Price Range Option View Model List size is ${priceRangeOptionList.size}, expected is $expectedPriceRangeOptionCount"
        }

        priceRangeOptionViewModelList.forEachIndexed { index, priceOptionViewModel ->
            priceOptionViewModel.assertPriceRangeOption(expectedPriceRangeOption[index], index + 1)
        }
    }

    private fun PriceOptionViewModel.assertPriceRangeOption(priceRangeOption: Option, expectedPosition: Int) {
        assert(option == priceRangeOption) {
            "Price Option View Model option is ${option.name}, expected is ${priceRangeOption.name}."
        }

        assert(position == expectedPosition) {
            "Price Option View Model Position is $position, expected is ${expectedPosition}."
        }
    }

    private fun Visitable<*>.assertAsPriceRangeCheckboxDataView(expectedFilter: Filter) {
        val filterRangeCheckboxUiModel = this as PriceRangeFilterCheckboxDataView
        val actualKeywordFilter = filterRangeCheckboxUiModel.filter
        assert(actualKeywordFilter == expectedFilter) {
            "Keyword filter is $actualKeywordFilter.\nExpected $expectedFilter"
        }

        val priceRangeItemList = filterRangeCheckboxUiModel.optionViewModelList
        val priceRangeOptions = expectedFilter.options
        val priceRangeOptionCount = priceRangeOptions.size
        assert(priceRangeItemList.size == priceRangeOptionCount) {
            "Price range checkbox item size is ${priceRangeItemList.size}, " +
                "expected is $priceRangeOptionCount"
        }

        priceRangeItemList.forEach { priceRangeItem ->
            priceRangeItem.assertOptionViewModel(priceRangeItem.option)
        }
    }

    private fun Visitable<*>.assertAsKeywordFilterDataView(expectedFilter: Filter) {
        val keywordFilterDataView = this as KeywordFilterDataView
        val actualKeywordFilter = keywordFilterDataView.filter
        assert(actualKeywordFilter == expectedFilter) {
            "Keyword filter is $actualKeywordFilter.\nExpected $expectedFilter"
        }

        val keywordFilterItemList = keywordFilterDataView.itemList
        val negativeKeywordOptions = expectedFilter.options.filter {
            it.key == Option.KEY_NEGATIVE_KEYWORD
        }
        val negativeKeywordCount = negativeKeywordOptions.size
        assert(keywordFilterItemList.size == negativeKeywordCount) {
            "Keyword filter item size is ${keywordFilterItemList.size}, " +
                "expected is $negativeKeywordCount"
        }

        keywordFilterItemList.forEachIndexed { index, keywordFilterItem ->
            keywordFilterItem.assertKeywordFilterItem(negativeKeywordOptions[index])
        }
    }

    private fun KeywordFilterItemDataView.assertKeywordFilterItem(expectedOption: Option) {
        val actualNegativeKeyword = negativeKeyword
        assert(actualNegativeKeyword == expectedOption.name) {
            "Negative keyword is $negativeKeyword.\nExpected is ${expectedOption.name}"
        }
    }

    private fun Visitable<*>.assertAsFilterViewModel(expectedFilter: Filter) {
        val filterViewModel = (this as FilterViewModel)
        val actualFilterInFilterViewModel = filterViewModel.filter
        assert(actualFilterInFilterViewModel == expectedFilter) {
            "Filter data is $actualFilterInFilterViewModel.\nExpected $expectedFilter"
        }

        val hasNotPopularOption = filterViewModel.filter.options.any { !it.isPopular }
        val expectedHasSeeAllButton = hasNotPopularOption || filterViewModel.filter.isCategoryFilter
        assert(filterViewModel.hasSeeAllButton == expectedHasSeeAllButton) {
            "Filter ${filterViewModel.filter.title} HasSeeAllButton should be $expectedHasSeeAllButton."
        }

        val optionViewModelList = filterViewModel.optionViewModelList
        val popularOptionList = expectedFilter.getPopularOptions()

        assert(optionViewModelList.size == popularOptionList.size) {
            "Option View Model List size is ${optionViewModelList.size}, expected is ${popularOptionList.size}"
        }

        optionViewModelList.forEachIndexed { index, optionViewModel ->
            optionViewModel.assertOptionViewModel(popularOptionList[index])
        }
    }

    private fun Filter.getPopularOptions(): List<Option> {
        val optionList = options
        val levelTwoCategoryOptionList = optionList.map { it.levelTwoCategoryList }.flatten()
        val levelThreeCategoryOptionList = levelTwoCategoryOptionList.map { it.levelThreeCategoryList }.flatten()

        return options.filter { it.isPopular } +
                levelTwoCategoryOptionList.filter { it.isPopular }.map { it.asOption() } +
                levelThreeCategoryOptionList.filter { it.isPopular }.map { it.asOption() }
    }

    private fun OptionViewModel.assertOptionViewModel(expectedOption: Option) {
        val actualOptionInOptionViewModel = this.option
        assert(actualOptionInOptionViewModel == expectedOption) {
            "Option data is ${actualOptionInOptionViewModel.uniqueId}.\nExpected ${expectedOption.uniqueId}"
        }
    }

    @Test
    fun `onViewCreated with Dynamic Filter Model and given selected sort map`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val selectedSort = dynamicFilterModel.data.sort[3]
        val mapParameter = mutableMapOf<String, String>().also {
            it[selectedSort.key] = selectedSort.value
        }

        `Given initialized SortFilterBottomSheetViewModel`(mapParameter, dynamicFilterModel)

        `When view created`()

        `Then assert sort item view model is sorted with selected sort as first item`(selectedSort)
        `Then assert button reset visibility`(true)
    }

    private fun `Then assert sort item view model is sorted with selected sort as first item`(selectedSort: Sort) {
        val sortFilterList = this.sortFilterList!!

        val sortViewModel = sortFilterList.findAndReturn<SortViewModel>()
                ?: throw AssertionError("Sort View Model should not be null")

        val sortItemViewModel = sortViewModel.sortItemViewModelList[0]
        sortItemViewModel.assertSortItemViewModel(selectedSort)
        assert(sortItemViewModel.isSelected) {
            "Sort Item View Model is selected should be true"
        }
    }

    @Test
    fun `onViewCreated with given Dynamic Filter Model and selected filter map`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val selectedFilterLocation = dynamicFilterModel.data.filter[0]
        val selectedOptionJabodetabek = selectedFilterLocation.options[1]
        val selectedOptionBali = selectedFilterLocation.options[4]
        val mapParameter = mutableMapOf<String, String>().also {
            it[selectedOptionJabodetabek.key] =
                    selectedOptionJabodetabek.value + OPTION_SEPARATOR + selectedOptionBali.value
        }

        `Given initialized SortFilterBottomSheetViewModel`(mapParameter, dynamicFilterModel)

        `When view created`()

        `Then assert button reset visibility`(true)
        val filterViewModel = this.sortFilterList!!.findFilterViewModel(selectedFilterLocation)!!
        `Then assert option view model list selected state`(filterViewModel, listOf(selectedOptionJabodetabek, selectedOptionBali), MAX_OPTION_SIZE)
        `Then assert option is sorted`(filterViewModel)
    }

    @Test
    fun `onViewCreated with given Dynamic Filter Model and selected not popular option`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val selectedFilter = dynamicFilterModel.data.filter[0]
        val selectedNotPopularOption = selectedFilter.options.find { !it.isPopular }!! // Just find any option that is not popular for testing
        val mapParameter = mutableMapOf<String, String>().also {
            it[selectedNotPopularOption.key] = selectedNotPopularOption.value
        }

        `Given initialized SortFilterBottomSheetViewModel`(mapParameter, dynamicFilterModel)

        `When view created`()

        `Then assert button reset visibility`(true)
        val filterViewModel = this.sortFilterList!!.findFilterViewModel(selectedFilter)!!
        `Then assert option view model list selected state`(filterViewModel, listOf(selectedNotPopularOption), MAX_OPTION_SIZE)
        `Then assert option is sorted`(filterViewModel)
    }

    @Test
    fun `onViewCreated with given Dynamic Filter Model and selected not popular option more than MAX_OPTION_SIZE`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val selectedFilter = dynamicFilterModel.data.filter[0]
        val selectedNotPopularOption = selectedFilter.options.filter { !it.isPopular }.take(6) // Just find any option that is not popular for testing
        val selectedNotPopularOptionKey = selectedNotPopularOption[0].key
        val selectedNotPopularOptionValues = selectedNotPopularOption.joinToString(separator = OPTION_SEPARATOR) { it.value }

        val mapParameter = mapOf(selectedNotPopularOptionKey to selectedNotPopularOptionValues)

        `Given initialized SortFilterBottomSheetViewModel`(mapParameter, dynamicFilterModel)

        `When view created`()

        `Then assert button reset visibility`(true)
        val filterViewModel = this.sortFilterList!!.findFilterViewModel(selectedFilter)!!
        `Then assert option view model list selected state`(filterViewModel, selectedNotPopularOption, 6)
        `Then assert option is sorted`(filterViewModel)
    }

    @Test
    fun `onViewCreated with given Dynamic Filter Model and selected level two category option`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val selectedFilter = dynamicFilterModel.data.filter[9]
        // Just find any filter category level two option for testing
        val selectedLevelTwoOption = selectedFilter.options
                .map { it.levelTwoCategoryList }.flatten()[1]
        val mapParameter = mutableMapOf<String, String>().also {
            it[selectedLevelTwoOption.key] = selectedLevelTwoOption.value
        }

        `Given initialized SortFilterBottomSheetViewModel`(mapParameter, dynamicFilterModel)

        `When view created`()

        `Then assert button reset visibility`(true)
        val filterViewModel = this.sortFilterList!!.findFilterViewModel(selectedFilter)!!
        `Then assert option view model list selected state`(filterViewModel, listOf(selectedLevelTwoOption.asOption()), MAX_OPTION_SIZE)
        `Then assert option is sorted`(filterViewModel)
        `Then assert selected category filter value`(selectedLevelTwoOption.value)
    }

    private fun `Then assert selected category filter value`(expectedCategoryFilterValue: String) {
        val selectedCategoryFilterValue = sortFilterBottomSheetViewModel.getSelectedCategoryFilterValue()
        assert(selectedCategoryFilterValue == expectedCategoryFilterValue) {
            "Selected Category Filter Value is $selectedCategoryFilterValue, expected is $expectedCategoryFilterValue."
        }
    }

    @Test
    fun `onViewCreated with given Dynamic Filter Model and selected level three category option`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val selectedFilter = dynamicFilterModel.data.filter[9]
        // Just find any filter category level two option for testing
        val selectedLevelThreeOption = selectedFilter.options
                .map { it.levelTwoCategoryList }.flatten()
                .map { it.levelThreeCategoryList }.flatten()[1]
        val mapParameter = mutableMapOf<String, String>().also {
            it[selectedLevelThreeOption.key] = selectedLevelThreeOption.value
        }

        `Given initialized SortFilterBottomSheetViewModel`(mapParameter, dynamicFilterModel)

        `When view created`()

        `Then assert button reset visibility`(true)
        val filterViewModel = this.sortFilterList!!.findFilterViewModel(selectedFilter)!!
        `Then assert option view model list selected state`(filterViewModel, listOf(selectedLevelThreeOption.asOption()), MAX_OPTION_SIZE)
        `Then assert option is sorted`(filterViewModel)
        `Then assert selected category filter value`(selectedLevelThreeOption.value)
    }

    @Test
    fun `onViewCreated with given Dynamic Filter Model and has price filter`() {
        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        val priceFilter = dynamicFilterModel.data.filter[2] // Price Filter
        val selectedPriceRange = priceFilter.options.find { it.key == Option.KEY_PRICE_RANGE_1 }!!
        val minPriceFilterValue = selectedPriceRange.valMin
        val maxPriceFilterValue = selectedPriceRange.valMax
        val mapParameter = mutableMapOf<String, String>().also {
            it[SearchApiConst.PMIN] = minPriceFilterValue
            it[SearchApiConst.PMAX] = maxPriceFilterValue
        }

        `Given initialized SortFilterBottomSheetViewModel`(mapParameter, dynamicFilterModel)

        `When view created`()

        val priceFilterViewModel = this.sortFilterList!!.findAndReturn<PriceFilterViewModel>()!!

        `Then assert button reset visibility`(true)
        `Then assert price filter value is correct`(priceFilterViewModel, minPriceFilterValue, maxPriceFilterValue)
        `Then assert price range option is selected`(priceFilterViewModel, selectedPriceRange.key)
    }

    private fun `Then assert price filter value is correct`(
            priceFilterViewModel: PriceFilterViewModel,
            expectedMinPriceFilterValue: String,
            expectedMaxPriceFilterValue: String
    ) {
        assert(priceFilterViewModel.minPriceFilterValue == expectedMinPriceFilterValue) {
            "Min Price Filter value should be $expectedMinPriceFilterValue"
        }

        assert(priceFilterViewModel.maxPriceFilterValue == expectedMaxPriceFilterValue) {
            "Max Price Filter value should be $expectedMaxPriceFilterValue"
        }
    }

    private fun `Then assert price range option is selected`(priceFilterViewModel: PriceFilterViewModel, selectedPriceRangeKey: String) {
        priceFilterViewModel.priceRangeOptionViewModelList.forEachIndexed { _, priceRangeOption ->
            val expectedIsSelected = priceRangeOption.option.key == selectedPriceRangeKey
            assert(expectedIsSelected == priceRangeOption.isSelected) {
                "Price range option ${priceRangeOption.option.name} is selected should be ${expectedIsSelected}."
            }
        }
    }

    @Test
    fun `Late init dynamic filter model`() {
        `Given null dynamic filter model and view is created`()

        val dynamicFilterModel = "dynamic-filter-model-common.json".jsonToObject<DynamicFilterModel>()
        `When late init dynamic filter model`(dynamicFilterModel)

        `Then assert is loading for dynamic filter`(false)
        `Then assert sort filter list is generated based on dynamic filter model`(dynamicFilterModel)
    }

    private fun `Given null dynamic filter model and view is created`() {
        sortFilterBottomSheetViewModel.init(mapOf(), null)
        sortFilterBottomSheetViewModel.onViewCreated()
    }

    private fun `When late init dynamic filter model`(dynamicFilterModel: DynamicFilterModel) {
        sortFilterBottomSheetViewModel.lateInitDynamicFilterModel(dynamicFilterModel)
    }
}
