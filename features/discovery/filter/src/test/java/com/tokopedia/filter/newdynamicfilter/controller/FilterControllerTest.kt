package com.tokopedia.filter.newdynamicfilter.controller

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.LevelThreeCategory
import com.tokopedia.filter.common.data.LevelTwoCategory
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import org.junit.Test
import java.util.*

class FilterControllerTest {

    companion object {
        private const val QUERY_FOR_TEST_SAMSUNG = "samsung"

        private const val TRUE_VALUE = true.toString()

        private const val JABODETABEK_VALUE = "1,2,3,4,5,6,7,8,9"
        private const val JAKARTA_VALUE = "1,2,3,4,5"
        private const val JAKARTA_BARAT_VALUE = "1"
        private const val TANGERANG_VALUE = "8"
        private const val BANDUNG_VALUE = "10"

        private const val HANDPHONE_VALUE = "1"
        private const val SEMUA_HANDPHONE_VALUE = "1"
        private const val HANDPHONE_BESAR_VALUE = "2"
        private const val SEMUA_HANDPHONE_BESAR_VALUE = "2"
        private const val HANDPHONE_ENAM_INCH = "3"
        private const val TV_VALUE = "4"
        private const val TV_LCD_VALUE = "5"
    }

    private val filterController = FilterController()

    private val officialOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.OFFICIAL, TRUE_VALUE, SearchApiConst.OFFICIAL))

    private val jabodetabekOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, JABODETABEK_VALUE, "Jabodetabek"))
    private val jakartaOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, JAKARTA_VALUE, "Jakarta"))
    private val jakartaBaratOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, JAKARTA_BARAT_VALUE, "Jakarta Barat"))
    private val tangerangOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, TANGERANG_VALUE, "Tangerang"))
    private val bandungOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.FCITY, BANDUNG_VALUE, "Bandung"))
    private val semuaHandphoneOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.SC, SEMUA_HANDPHONE_VALUE, "Semua Handphone")).also {
        it.isPopular = true
    }
    private val handphoneBesarOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.SC, HANDPHONE_BESAR_VALUE, "Handphone Besar"))
    private val semuaHandphoneBesarOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.SC, SEMUA_HANDPHONE_BESAR_VALUE, "Semua Handphone Besar"))
    private val handphoneEnamInchOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.SC, HANDPHONE_ENAM_INCH, "Handphone Enam Inch")).also {
        it.isPopular = true
    }
    private val handphoneOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.SC, HANDPHONE_VALUE, "Handphone")).also{
        it.levelTwoCategoryList = createHandphoneCategoryLevels()
        it.isPopular = true
    }
    private val tvLCDOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.SC, TV_LCD_VALUE, "TV"))
    private val tvOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.SC, TV_VALUE, "TV")).also {
        it.levelTwoCategoryList = createTVCategoryLevels()
        it.isPopular = true
    }

    private val minPriceOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.PMIN, "0", "Harga Minimum")).also {
        it.inputType = Option.INPUT_TYPE_TEXTBOX
    }
    private val maxPriceOption = OptionHelper.generateOptionFromUniqueId(OptionHelper.constructUniqueId(SearchApiConst.PMAX, "", "Harga Maximum")).also {
        it.inputType = Option.INPUT_TYPE_TEXTBOX
    }

    private val tokoOptions = mutableListOf<Option>()
    private val locationOptions = mutableListOf<Option>()
    private val categoryOptions = mutableListOf<Option>()
    private val priceOptions = mutableListOf<Option>()

    private fun createHandphoneCategoryLevels() : List<LevelTwoCategory> {
        val handphoneLevelTwoCategoryList = mutableListOf<LevelTwoCategory>()

        val semuaHandphoneLevelTwoCategory = createLevelTwoCategory(semuaHandphoneOption, listOf())
        val handphoneBesarLevelTwoCategory = createLevelTwoCategory(handphoneBesarOption, mutableListOf<Option>().also { levelThreeOptionList ->
            levelThreeOptionList.add(semuaHandphoneBesarOption)
            levelThreeOptionList.add(handphoneEnamInchOption)
        })

        handphoneLevelTwoCategoryList.add(semuaHandphoneLevelTwoCategory)
        handphoneLevelTwoCategoryList.add(handphoneBesarLevelTwoCategory)

        return handphoneLevelTwoCategoryList
    }

    private fun createTVCategoryLevels() : List<LevelTwoCategory> {
        val tvLevelTwoCharCategory = mutableListOf<LevelTwoCategory>()
        tvLevelTwoCharCategory.add(createLevelTwoCategory(tvLCDOption, listOf()))
        return tvLevelTwoCharCategory
    }

    private fun createLevelTwoCategory(option: Option, levelThreeCategoryOptionList: List<Option>) : LevelTwoCategory {
        return LevelTwoCategory().apply {
            key = option.key
            value = option.value
            name = option.name
            isPopular = option.isPopular
            levelThreeCategoryList = createLevelThreeCategoryList(levelThreeCategoryOptionList)
        }
    }

    private fun createLevelThreeCategoryList(optionListLevelThree: List<Option>) : List<LevelThreeCategory> {
        val levelThreeCategoryList = mutableListOf<LevelThreeCategory>()

        for(option in optionListLevelThree) {
            levelThreeCategoryList.add(createLevelThreeCategory(option))
        }

        return levelThreeCategoryList
    }

    private fun createLevelThreeCategory(option: Option) : LevelThreeCategory {
        return LevelThreeCategory().apply {
            key = option.key
            value = option.value
            name = option.name
            isPopular = option.isPopular
        }
    }

    @Test
    fun testInitFilterWithNullsOrEmptyShouldNotCrash() {
        filterController.initFilterController(null, null)
        filterController.initFilterController(null, listOf())
        filterController.initFilterController(mapOf(), null)
        filterController.initFilterController()
    }

    @Test
    fun testSetFilterWithNullOptionShouldNotCrash() {
        filterController.initFilterController()

        filterController.setFilter(null, true)
        filterController.setFilter(null)
        filterController.setFilter(listOf())
    }

    @Test
    fun testFilterControllerInitializedProperly() {
        val filterParameter = createParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)

        assertFilterViewStateSizeCorrect(1)
        assertFilterViewStateCorrect(listOf(officialOption))
    }

    private fun createParameter() : Map<String, String> {
        val parameter = mutableMapOf<String, String>()
        parameter[SearchApiConst.Q] = QUERY_FOR_TEST_SAMSUNG
        parameter[SearchApiConst.OFFICIAL] = TRUE_VALUE

        return parameter
    }

    private fun createFilterList() : List<Filter> {
        val filterList = mutableListOf<Filter>()

        tokoOptions.add(officialOption)
        filterList.add(createFilterWithOptions(tokoOptions))

        locationOptions.add(jabodetabekOption)
        locationOptions.add(jakartaOption)
        locationOptions.add(jakartaBaratOption)
        locationOptions.add(tangerangOption)
        locationOptions.add(bandungOption)
        filterList.add(createFilterWithOptions(locationOptions))

        categoryOptions.add(handphoneOption)
        categoryOptions.add(tvOption)
        filterList.add(createFilterWithOptions(categoryOptions))

        priceOptions.add(minPriceOption)
        priceOptions.add(maxPriceOption)
        filterList.add(createFilterWithOptions(priceOptions))

        return filterList
    }

    private fun createFilterWithOptions(optionList: List<Option>) : Filter {
        val filter = Filter()
        filter.options = optionList
        return filter
    }

    @Test
    fun testFilterControllerInitializedUsingOptionsWithEmptyValueAndTypeTextBox() {
        val filterParameter = HashMap<String, String>(createParameter())
        filterParameter[minPriceOption.key] = 1000.toString()
        filterParameter[maxPriceOption.key] = 10000.toString()

        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)

        assertFilterControllerInitializedUsingOptionsWithEmptyValue()
    }

    private fun assertFilterControllerInitializedUsingOptionsWithEmptyValue() {
        val expectedOptionList = mutableListOf<Option>()
        expectedOptionList.add(officialOption)
        expectedOptionList.add(createPriceOptionWithValue(minPriceOption, 1000))
        expectedOptionList.add(createPriceOptionWithValue(maxPriceOption, 10000))

        assertFilterViewStateSizeCorrect(2) // Expected is 2, min and max price option counted as 1
        assertFilterViewStateCorrect(expectedOptionList)
    }

    private fun createPriceOptionWithValue(priceOption: Option, priceOptionValue: Int) : Option {
        val createdPriceOption = OptionHelper.generateOptionFromUniqueId(priceOption.uniqueId)
        createdPriceOption.value = priceOptionValue.toString()

        return createdPriceOption
    }

    @Test
    fun testLoadFilterViewStateWithBundledOptions() {
        prepareLocationOptions()

        val locationOptionPermutations = getAllOptionPermutations(locationOptions.toTypedArray())!!
        locationOptionPermutations.forEach { locationOptionPermutation ->
            val locationOptionPermutationList = createOptionsFromPermutations(locationOptionPermutation)

            testLoadFilterViewStateWithBundledLocationOptions(locationOptionPermutationList)
        }
    }

    private fun prepareLocationOptions() {
        locationOptions.add(jabodetabekOption)
        locationOptions.add(jakartaOption)
        locationOptions.add(jakartaBaratOption)
        locationOptions.add(tangerangOption)
        locationOptions.add(bandungOption)
    }

    private fun getAllOptionPermutations(optionList: Array<Option>?): Set<Array<Option?>>? {
        if (optionList == null)
            return null

        val perms = mutableSetOf<Array<Option?>>()

        if (optionList.isEmpty()) {
            perms.add(arrayOfNulls(0))
            return perms
        }

        val first = optionList[0]
        val remainder = Arrays.copyOfRange(optionList, 1, optionList.size)
        val subPerms = getAllOptionPermutations(remainder)
        for (subPerm in subPerms!!) {
            for (i in 0..subPerm.size) {
                val newPerm = subPerm.copyOf(subPerm.size + 1)
                for (j in newPerm.size - 1 downTo i + 1)
                    newPerm[j] = newPerm[j - 1]
                newPerm[i] = first
                perms.add(newPerm)
            }
        }

        return perms
    }

    private fun createOptionsFromPermutations(optionArray: Array<Option?>) : List<Option> {
        val optionList = mutableListOf<Option>()

        for(option in optionArray) {
            if(option == null) continue

            optionList.add(option)
        }

        return optionList
    }

    private fun testLoadFilterViewStateWithBundledLocationOptions(locationOptions: List<Option>) {
        printLocationOptionsName(locationOptions)

        val filterParameter = createFilterParameterWithBundledOption()
        val filterList = createFilterListWithVariousLocationOptions(locationOptions)

        filterController.initFilterController(filterParameter, filterList)

        assertFilterViewStateOnlyHasBundledOptions()

        printTestPassed()
    }

    private fun printLocationOptionsName(locationOptions: List<Option>) {
        print("Testing location option with combination: ")

        val locationOptionNames = mutableListOf<String>()
        locationOptions.forEach { locationOptionNames.add(it.name) }

        print(locationOptionNames.joinToString())
    }

    private fun createFilterParameterWithBundledOption() : Map<String, String> {
        val filterParameter = mutableMapOf<String, String>()
        filterParameter[SearchApiConst.Q] = QUERY_FOR_TEST_SAMSUNG
        filterParameter[SearchApiConst.OFFICIAL] = TRUE_VALUE
        filterParameter[SearchApiConst.FCITY] = "${jabodetabekOption.value}${OptionHelper.OPTION_SEPARATOR}${bandungOption.value}"

        return filterParameter
    }

    private fun createFilterListWithVariousLocationOptions(locationOptions : List<Option>) : List<Filter> {
        val filterList = mutableListOf<Filter>()

        tokoOptions.add(officialOption)
        filterList.add(createFilterWithOptions(tokoOptions))

        filterList.add(createFilterWithOptions(locationOptions))

        categoryOptions.add(handphoneOption)
        categoryOptions.add(tvOption)
        filterList.add(createFilterWithOptions(categoryOptions))

        priceOptions.add(minPriceOption)
        priceOptions.add(maxPriceOption)
        filterList.add(createFilterWithOptions(priceOptions))

        return filterList
    }

    private fun assertFilterViewStateOnlyHasBundledOptions() {
        val expectedOptions = mutableListOf<Option>()
        expectedOptions.add(jabodetabekOption)
        expectedOptions.add(bandungOption)
        expectedOptions.add(officialOption)

        assertFilterViewStateSizeCorrect(expectedOptions.size)
        assertFilterViewStateCorrect(expectedOptions)
    }

    private fun printTestPassed() {
        println(".... Passed")
    }

    @Test
    fun `test multiple option selected by hashtag`() {
        val parameter = mapOf(
                SearchApiConst.Q to QUERY_FOR_TEST_SAMSUNG,
                SearchApiConst.FCITY to "${jakartaOption.value}${OptionHelper.OPTION_SEPARATOR}${jabodetabekOption.value}"
        )

        filterController.initFilterController(parameter, createFilterList())

        assertFilterViewStateSizeCorrect(2)
        assertFilterViewStateCorrect(listOf(jabodetabekOption, jakartaOption))
    }

    @Test
    fun testLoadFilterViewStateWithLevelTwoCategory() {
        val filterParameter = HashMap<String, String>(createParameter())
        filterParameter[semuaHandphoneOption.key] = semuaHandphoneOption.value

        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)

        assertFilterViewStateIncludesLevelTwoCategory()
    }

    private fun assertFilterViewStateIncludesLevelTwoCategory() {
        val expectedOptionList = mutableListOf<Option>()
        expectedOptionList.add(officialOption)
        expectedOptionList.add(handphoneOption)

        assertFilterViewStateSizeCorrect(expectedOptionList.size)
        assertFilterViewStateCorrect(expectedOptionList)
    }

    @Test
    fun testLoadFilterViewStateWithLevelThreeCategoryExpectingLevelTwo() {
        val filterParameter = HashMap<String, String>(createParameter())
        filterParameter[semuaHandphoneBesarOption.key] = semuaHandphoneBesarOption.value

        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)

        assertFilterViewStateIncludesLevelThreeCategoryExpectingLevelTwo()
    }

    private fun assertFilterViewStateIncludesLevelThreeCategoryExpectingLevelTwo() {
        val expectedOptionList = mutableListOf<Option>()
        expectedOptionList.add(officialOption)
        expectedOptionList.add(handphoneBesarOption)

        assertFilterViewStateSizeCorrect(expectedOptionList.size)
        assertFilterViewStateCorrect(expectedOptionList)
    }

    @Test
    fun testLoadFilterViewStateWithLevelThreeCategory() {
        val filterParameter = HashMap<String, String>(createParameter())
        filterParameter[handphoneEnamInchOption.key] = handphoneEnamInchOption.value

        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)

        assertFilterViewStateIncludesLevelThreeCategory()
    }

    private fun assertFilterViewStateIncludesLevelThreeCategory() {
        val expectedOptionList = mutableListOf<Option>()
        expectedOptionList.add(officialOption)
        expectedOptionList.add(handphoneEnamInchOption)

        assertFilterViewStateSizeCorrect(expectedOptionList.size)
        assertFilterViewStateCorrect(expectedOptionList)
    }

    @Test
    fun testSaveSliderValues() {
        val originalSliderMinStateValue = 1
        val originalSliderMaxStateValue = 1000

        filterController.initFilterController()
        filterController.saveSliderValueStates(originalSliderMinStateValue, originalSliderMaxStateValue)

        assertSliderValueHasChanged(2, 1000, true)
        assertSliderValueHasChanged(1, 2000, true)
        assertSliderValueHasChanged(1, 1000, false)
    }

    private fun assertSliderValueHasChanged(newSliderMinStateValue: Int, newSliderMaxStateValue: Int, expectedIsSliderValueHasChanged: Boolean) {
        val actualIsSliderValueHasChanged = filterController.isSliderValueHasChanged(newSliderMinStateValue, newSliderMaxStateValue)

        assert(actualIsSliderValueHasChanged == expectedIsSliderValueHasChanged) {
            getAssertSliderValueHasChangedMessage(newSliderMinStateValue, newSliderMaxStateValue, expectedIsSliderValueHasChanged)
        }
    }

    private fun getAssertSliderValueHasChangedMessage(newSliderMinStateValue: Int, newSliderMaxStateValue: Int, expectedIsSliderValueHasChanged: Boolean) : String {
        return "Testing slider value should be considered changed: $expectedIsSliderValueHasChanged.\n" +
                "New slider state values: $newSliderMinStateValue - $newSliderMaxStateValue"
    }

    @Test
    fun testResetAllFilters() {
        val filterParameter = createParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)
        filterController.saveSliderValueStates(1, 1000)

        filterController.resetAllFilters()

        assertResetAllFiltersCorrect()
    }

    private fun assertResetAllFiltersCorrect() {
        val actualParameter = filterController.getParameter()
        val expectedParameterSize = 1
        val expectedParameterKey = SearchApiConst.Q
        val expectedParameterContainsKey = true

        assert(actualParameter.size == expectedParameterSize
                && actualParameter.contains(expectedParameterKey)) {
            "Testing reset all filters:\n" +
                    "Parameter expected size: $expectedParameterSize, actual size: ${actualParameter.size}\n" +
                    "Parameter should contain key: $expectedParameterKey, expected: $expectedParameterContainsKey, actual: ${actualParameter.contains(expectedParameterKey)}"
        }

        val actualFilterViewStateSize = filterController.getFilterCount()
        assert(actualFilterViewStateSize == 0) {
            "Testing reset all filters:\n" +
                    "Filter View State expected size: 0, actual size: $actualFilterViewStateSize"
        }

        assertSliderValueHasChanged(-1, -1, false)
    }

    @Test
    fun testSetFilterNoCleanUp() {
        val filterParameter = createParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)
        filterController.setFilter(jabodetabekOption, true)
        filterController.setFilter(jakartaOption, true)

        assertFilterViewStatesAppended()
    }

    private fun assertFilterViewStatesAppended() {
        val expectedOptions = mutableListOf<Option>()
        expectedOptions.add(jabodetabekOption)
        expectedOptions.add(jakartaOption)
        expectedOptions.add(officialOption)

        assertFilterViewStateCorrect(expectedOptions)
        assertFilterViewStateSizeCorrect(expectedOptions.size)
    }

    @Test
    fun testSetFilterWithCleanUp() {
        val filterParameter = createParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)
        filterController.setFilter(handphoneOption, true, isCleanUpExistingFilterWithSameKey = true)
        filterController.setFilter(tvOption, true, isCleanUpExistingFilterWithSameKey = true)
        filterController.setFilter(createPriceOptionWithValue(minPriceOption, 900), true, isCleanUpExistingFilterWithSameKey = true)
        filterController.setFilter(createPriceOptionWithValue(minPriceOption, 1000), true, isCleanUpExistingFilterWithSameKey = true)

        assertFilterViewStateReplaced()
    }

    private fun assertFilterViewStateReplaced() {
        val expectedOptions = mutableListOf<Option>()
        expectedOptions.add(tvOption)
        expectedOptions.add(officialOption)
        expectedOptions.add(createPriceOptionWithValue(minPriceOption, 1000))

        assertFilterValueCorrect(expectedOptions)
        assertFilterViewStateCorrect(expectedOptions)
        assertFilterViewStateSizeCorrect(expectedOptions.size)
    }

    @Test
    fun testRemoveFilter() {
        val filterParameter = createParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)
        filterController.setFilter(officialOption, false)

        assertFilterViewStateRemoved()
    }

    private fun assertFilterViewStateRemoved() {
        assertFilterViewStateCorrect(listOf())
        assertFilterViewStateSizeCorrect(0)
    }

    @Test
    fun testSetFilterMultipleOptions() {
        val filterParameter = createParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)

        val optionsWithInputState = createMultipleOptionsWithInputState()
        filterController.setFilter(optionsWithInputState)

        assertFilterViewStateMultipleOptions()
    }

    private fun createMultipleOptionsWithInputState() : List<Option> {
        val optionList = mutableListOf<Option>()

        val jabodetabekOptionSelected = OptionHelper.generateOptionFromUniqueId(jabodetabekOption.uniqueId)
        jabodetabekOptionSelected.inputState = TRUE_VALUE
        optionList.add(jabodetabekOptionSelected)

        val jakartaOptionSelected = OptionHelper.generateOptionFromUniqueId(jakartaOption.uniqueId)
        jakartaOptionSelected.inputState = TRUE_VALUE
        optionList.add(jakartaOptionSelected)

        val jakartaBaratOptionUnSelected = OptionHelper.generateOptionFromUniqueId(jakartaBaratOption.uniqueId)
        jakartaBaratOptionUnSelected.inputState = "Some random string should NOT make this option selected"
        optionList.add(jakartaBaratOptionUnSelected)

        return optionList
    }

    private fun assertFilterViewStateMultipleOptions() {
        val expectedOptions = mutableListOf<Option>()
        expectedOptions.add(jabodetabekOption)
        expectedOptions.add(jakartaOption)
        expectedOptions.add(officialOption)

        assertFilterViewStateCorrect(expectedOptions)
        assertFilterViewStateSizeCorrect(expectedOptions.size)
    }

    @Test
    fun testSetFilterMultipleOptionsShouldReplacePreviouslySelectedOptionWithSameKey() {
        val filterParameter = createParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)
        filterController.setFilter(handphoneOption, true, isCleanUpExistingFilterWithSameKey = true)
        filterController.setFilter(jakartaBaratOption, true)

        val optionsWithInputState = createMultipleOptionsWithInputState()
        filterController.setFilter(optionsWithInputState)

        assertSetFilterMultipleOptionsShouldReplacePreviouslySelectedOptionWithSameKey()
    }

    private fun assertSetFilterMultipleOptionsShouldReplacePreviouslySelectedOptionWithSameKey() {
        val expectedOptions = mutableListOf<Option>()
        expectedOptions.add(jabodetabekOption)
        expectedOptions.add(jakartaOption)
        expectedOptions.add(officialOption)
        expectedOptions.add(handphoneOption)

        assertFilterViewStateCorrect(expectedOptions)
        assertFilterViewStateSizeCorrect(expectedOptions.size)
    }

    @Test
    fun testGetActiveFilterAsOptionList() {
        val filterParameter = createParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)
        filterController.setFilter(createPriceOptionWithValue(minPriceOption, 1000), true, isCleanUpExistingFilterWithSameKey = true)
        filterController.setFilter(jakartaOption, true)
        filterController.setFilter(handphoneOption, true, isCleanUpExistingFilterWithSameKey = true)

        val expectedOptionList = mutableListOf<Option>()
        expectedOptionList.add(officialOption)
        expectedOptionList.add(minPriceOption)
        expectedOptionList.add(jakartaOption)
        expectedOptionList.add(handphoneOption)

        assertActiveFilterOptionList(expectedOptionList)
    }

    @Test
    fun testGetActiveFilterAsOptionListWithCategoryOption() {
        val filterParameter = createParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)
        filterController.setFilter(handphoneEnamInchOption, true, isCleanUpExistingFilterWithSameKey = true)

        val expectedOptionList = mutableListOf<Option>()
        expectedOptionList.add(officialOption)
        expectedOptionList.add(handphoneEnamInchOption)

        assertActiveFilterOptionList(expectedOptionList)
    }

    @Test
    fun testGetActiveFilterAsMap() {
        val filterParameter = createParameter()
        val filterList = createFilterList()

        filterController.initFilterController(filterParameter, filterList)
        filterController.setFilter(createPriceOptionWithValue(maxPriceOption, 3000000), true, isCleanUpExistingFilterWithSameKey = true)
        filterController.setFilter(jakartaOption, true)
        filterController.setFilter(handphoneOption, true, isCleanUpExistingFilterWithSameKey = true)
        filterController.setFilter(jakartaBaratOption, true)

        val expectedMap = mutableMapOf<String, String>()
        expectedMap[officialOption.key] = officialOption.value
        expectedMap[maxPriceOption.key] = 3000000.toString()
        expectedMap[SearchApiConst.FCITY] = "${jakartaOption.value}${OptionHelper.OPTION_SEPARATOR}${jakartaBaratOption.value}"
        expectedMap[handphoneOption.key] = handphoneOption.value

        assertActiveFilterMap(expectedMap)
    }

    @Test
    fun `active filter map with same value should not have separator`() {
        val sameOptionValue = "1234"
        val susuOption = Option(key = SearchApiConst.SC, value = sameOptionValue, name = "susu")
        val semuaSusuOption = Option(key = SearchApiConst.SC, value = sameOptionValue, name = "semua susu")

        val filterParameter = mapOf(
                susuOption.key to susuOption.value,
                semuaSusuOption.key to semuaSusuOption.value,
        )
        val filterList = listOf(Filter(options = listOf(susuOption, semuaSusuOption)))

        filterController.initFilterController(filterParameter, filterList)

        val expectedMap = mutableMapOf<String, String>()
        expectedMap[SearchApiConst.SC] = sameOptionValue

        assertActiveFilterMap(expectedMap)
    }

    private fun assertFilterValueCorrect(optionList: List<Option>) {
        for(option in optionList) {
            val actualFilterValue = filterController.getFilterValue(option.key)
            assert(actualFilterValue == option.value) {
                getAssertFilterValueMessage(option.uniqueId, option.value, actualFilterValue)
            }
        }
    }

    private fun getAssertFilterValueMessage(uniqueId: String, expectedValue: String, actualValue: String) : String {
        return "Testing filter value, option $uniqueId: expected: $expectedValue, actual: $actualValue"
    }

    private fun assertFilterViewStateCorrect(optionList: List<Option>) {
        for(option in optionList) {
            val actualFilterViewState = filterController.getFilterViewState(option)
            assert(actualFilterViewState) {
                getAssertFilterViewStateMessage(option.uniqueId, true, actualFilterViewState)
            }
        }
    }

    private fun getAssertFilterViewStateMessage(uniqueId: String, expectedFilterViewState: Boolean, actualFilterViewState: Boolean) : String {
        return "Testing filter view state, option $uniqueId: expected: $expectedFilterViewState, actual: $actualFilterViewState"
    }

    private fun assertFilterViewStateSizeCorrect(expectedSize: Int) {
        val actualSize = filterController.getFilterCount()

        assert(filterController.getFilterCount() == expectedSize) {
            getAssertFilterViewStateSizeMessage(expectedSize, actualSize)
        }
    }

    private fun getAssertFilterViewStateSizeMessage(expectedFilterViewStateSize: Int, actualFitlerViewStateSize: Int) : String {
        return "Testing filter view state size, expected: $expectedFilterViewStateSize, actual $actualFitlerViewStateSize"
    }

    private fun assertActiveFilterOptionList(expectedOptionList: List<Option>) {
        val actualOptionList = filterController.getActiveFilterOptionList()

        assert(actualOptionList.size == expectedOptionList.size) {
            "Testing get active filter option list, expected size: ${expectedOptionList.size} actual size: ${actualOptionList.size}"
        }

        for(actualFilterOption in actualOptionList) {
            val isExpectedContainsActual = expectedOptionList.any {
                it.key == actualFilterOption.key
                        && it.name == actualFilterOption.name
            }

            assert(isExpectedContainsActual) {
                "Testing get active filter option list, option ${actualFilterOption.key} is expected."
            }
        }
    }

    private fun assertActiveFilterMap(expectedMap : Map<String, String>) {
        val actualMap = filterController.getActiveFilterMap()

        assert(actualMap.size == expectedMap.size) {
            "Testing get active filter map, expected size: ${expectedMap.size} actual size: ${actualMap.size}"
        }

        for(expectedMapEntry in expectedMap.entries) {
            assert(actualMap.contains(expectedMapEntry.key)) {
                "Testing get active filter map, expected Map key ${expectedMapEntry.key} not found in actual Map"
            }

            assert(actualMap[expectedMapEntry.key] == expectedMapEntry.value) {
                "Testing get active filter map, comparing value for key ${expectedMapEntry.key}. Expected value: ${expectedMapEntry.value}, actual value: ${actualMap[expectedMapEntry.key]}"
            }
        }
    }

    @Test
    fun getSelectedAndPopularOptions() {
        categoryOptions.add(handphoneOption)
        categoryOptions.add(tvOption)
        val categoryFilter = createFilterWithOptions(categoryOptions)

        val optionList = filterController.getSelectedAndPopularOptions(categoryFilter)

        val expectedOptionList = mutableListOf<Option>().also {
            it.add(handphoneOption)
            it.add(tvOption)
            it.add(semuaHandphoneOption)
            it.add(handphoneEnamInchOption)
        }

        optionList.assertOptionsInOrder(expectedOptionList)
    }

    private fun List<Option>.assertOptionsInOrder(expectedOptionList: List<Option>) {
        expectedOptionList.forEachIndexed { index, option ->
            assert(option == this[index]) {
                "Option at index $index is: ${this[index].uniqueId}, expected: ${option.uniqueId}"
            }
        }

        assert(this.size == expectedOptionList.size) {
            "Actual list size ${this.size} is different with expected size ${expectedOptionList.size}"
        }
    }

    @Test
    fun `test appendFilterList`() {
        val parameter = createParameterWithJabodetabekOptionSelected()

        `Given initialized FilterController only with quick filters`(parameter)

        `When FilterController add new filter`(parameter)

        `Then verify FilterController getFilterViewState is refreshed with new filter`()
    }

    private fun `Given initialized FilterController only with quick filters`(parameter: Map<String, String>) {
        val quickFilterList = createQuickFilterList()

        filterController.initFilterController(parameter, quickFilterList)
    }

    private fun createParameterWithJabodetabekOptionSelected(): Map<String, String> {
        val parameter = mutableMapOf<String, String>()
        parameter[SearchApiConst.Q] = QUERY_FOR_TEST_SAMSUNG
        parameter[SearchApiConst.FCITY] = JABODETABEK_VALUE

        return parameter
    }

    private fun createQuickFilterList(): List<Filter> {
        // Quick filter usually has multiple filters with only 1 option per filter

        val jakartaOptionList = mutableListOf<Option>().also { it.add(jakartaOption) }
        val officialOptionList = mutableListOf<Option>().also { it.add(officialOption) }

        return mutableListOf<Filter>().also {
            it.add(createFilterWithOptions(jakartaOptionList))
            it.add(createFilterWithOptions(officialOptionList))
        }
    }

    private fun `When FilterController add new filter`(parameter: Map<String, String>) {
        val filterList = createFilterList()
        filterController.appendFilterList(parameter, filterList)
    }

    private fun `Then verify FilterController getFilterViewState is refreshed with new filter`() {
        assert(!filterController.getFilterViewState(jakartaOption)) {
            "Jakarta Option should not be selected anymore"
        }

        assert(filterController.getFilterViewState(jabodetabekOption)) {
            "Jabodetabek Option should now be selected"
        }
    }
}