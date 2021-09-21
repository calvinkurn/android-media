package com.tokopedia.filter.newdynamicfilter.controller

import android.text.TextUtils
import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.LevelThreeCategory
import com.tokopedia.filter.common.data.LevelTwoCategory
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper

open class FilterController {

    private val nonFilterParameter = mutableMapOf<String, String>()
    private val filterList = mutableListOf<Filter>()
    private val filterViewState = mutableSetOf<String>()
    private var pressedSliderMinValueState = -1
    private var pressedSliderMaxValueState = -1
    val filterViewStateSet: Set<String>
        get() = filterViewState

    fun initFilterController(parameter: Map<String, String>? = mapOf(),
                             filterList: List<Filter>? = listOf()) {
        resetStatesBeforeLoad()

        loadFilterList(filterList)
        loadParameter(parameter)
        loadFilterViewState(parameter)
    }

    private fun resetStatesBeforeLoad() {
        nonFilterParameter.clear()
        filterList.clear()
        filterViewState.clear()
        pressedSliderMaxValueState = -1
        pressedSliderMaxValueState = -1
    }

    private fun loadFilterList(filterList: List<Filter>?) {
        if(filterList == null) return

        this.filterList.addAll(filterList)
    }

    private fun loadParameter(parameter: Map<String, String>?) {
        if(parameter == null) return

        generateNonFilterParameter(parameter)
    }

    private fun generateNonFilterParameter(parameter: Map<String, String>) {
        val toNonFilterParameter = mutableMapOf<String, String>()

        for(entry in parameter.entries) {
            if(!getIsFilterParameter(entry)) {
                toNonFilterParameter[entry.key] = entry.value
            }
        }

        nonFilterParameter.putAll(toNonFilterParameter)
    }

    private fun getIsFilterParameter(parameterEntry: Map.Entry<String, String>) : Boolean {
        var isFilterParameter = false

        loopOptionsInFilterList { _, option ->
            if(option.key == parameterEntry.key) {
                isFilterParameter = true
                return@loopOptionsInFilterList
            }
        }

        return isFilterParameter
    }

    private fun loadFilterViewState(parameter: Map<String, String>?) {
        if(parameter == null) return

        val optionsForFilterViewState = mutableListOf<Option>()

        loopOptionsInFilterList { _, option ->
            if(isOptionSelected(parameter, option)) {
                optionsForFilterViewState.add(option)
            }
            else {
                addOptionsFromDeeperLevelCategory(parameter, option, optionsForFilterViewState)
            }
        }

        for(option in optionsForFilterViewState) {
            if(option.value == "" || option.isTypeTextBox) option.value = parameter[option.key].toString()
            filterViewState.add(option.uniqueId)
        }
    }

    private fun isOptionSelected(parameter: Map<String, String>, option: Option) : Boolean {
        return if(parameter.containsKey(option.key))
            return when {
                option.value == "" || option.isTypeTextBox -> true
                isOptionValuesExistsInFilterParameter(parameter, option) -> true
                else -> false
            }
        else false
    }

    private fun isOptionValuesExistsInFilterParameter(parameter: Map<String, String>, option: Option) : Boolean {
        val optionValues = option.value.split(OptionHelper.VALUE_SEPARATOR)
        val optionValuesInParameter = parameter[option.key]?.split(OptionHelper.OPTION_SEPARATOR)

        optionValuesInParameter?.forEach { optionValue ->
            val filterParameterValues = optionValue.split(OptionHelper.VALUE_SEPARATOR)

            if (filterParameterValues.size == optionValues.size && filterParameterValues.containsAll(optionValues)) return true
        }

        return false
    }

    private fun addOptionsFromDeeperLevelCategory(parameter: Map<String, String>, option: Option, optionsForFilterViewState: MutableList<Option>) {
        if(!listIsNullOrEmpty(option.levelTwoCategoryList)) {
            val categoryAsOption = getSelectedOptionLevelTwoCategoryList(parameter, option.levelTwoCategoryList)
            if(categoryAsOption != null) {
                optionsForFilterViewState.add(categoryAsOption)
            }
        }
    }

    private fun <T: Any> listIsNullOrEmpty(list : List<T>?) : Boolean {
        return list == null || list.isEmpty()
    }

    private fun getSelectedOptionLevelTwoCategoryList(parameter: Map<String, String>, levelTwoCategoryList: List<LevelTwoCategory>) : Option? {
        for(levelTwoCategory in levelTwoCategoryList) {
            if(parameter[levelTwoCategory.key] == levelTwoCategory.value) {
                return OptionHelper.generateOptionFromUniqueId(
                    OptionHelper.constructUniqueId(levelTwoCategory.key, levelTwoCategory.value, levelTwoCategory.name)
                )
            }
            else if(!listIsNullOrEmpty(levelTwoCategory.levelThreeCategoryList)) {
                val levelThreeCategoryAsOption = getSelectedOptionLevelThreeCategoryList(parameter, levelTwoCategory.levelThreeCategoryList)
                if(levelThreeCategoryAsOption != null)
                    return levelThreeCategoryAsOption
            }
        }

        return null
    }

    private fun getSelectedOptionLevelThreeCategoryList(parameter: Map<String, String>, levelThreeCategoryList: List<LevelThreeCategory>) : Option? {
        for(levelThreeCategory in levelThreeCategoryList)
            if(parameter[levelThreeCategory.key] == levelThreeCategory.value)
                return OptionHelper.generateOptionFromUniqueId(
                    OptionHelper.constructUniqueId(levelThreeCategory.key, levelThreeCategory.value, levelThreeCategory.name)
                )

        return null
    }

    private fun loopOptionsInFilterList(action: (filter: Filter, option: Option) -> Unit) {
        for(filter in filterList)
            for(option in filter.options)
                action(filter, option)
    }

    fun appendFilterList(parameter: Map<String, String>, filterList: List<Filter>) {
        nonFilterParameter.clear()
        filterViewState.clear()

        loadFilterList(filterList)
        loadParameter(parameter)
        loadFilterViewState(parameter)
    }

    fun refreshMapParameter(parameter: Map<String, String>) {
        nonFilterParameter.clear()
        filterViewState.clear()

        loadParameter(parameter)
        loadFilterViewState(parameter)
    }

    fun saveSliderValueStates(minValue: Int, maxValue: Int) {
        pressedSliderMinValueState = minValue
        pressedSliderMaxValueState = maxValue
    }

    fun isSliderValueHasChanged(minValue: Int, maxValue: Int): Boolean {
        return minValue != pressedSliderMinValueState || maxValue != pressedSliderMaxValueState
    }

    fun isSliderMinValueHasChanged(minValue: Int): Boolean {
        return minValue != pressedSliderMinValueState
    }

    fun isSliderMaxValueHasChanged(maxValue: Int): Boolean {
        return maxValue != pressedSliderMaxValueState
    }

    fun resetAllFilters() {
        filterViewState.clear()
        resetSliderStates()
    }

    private fun resetSliderStates() {
        pressedSliderMinValueState = -1
        pressedSliderMaxValueState = -1
    }

    fun getSelectedOptions(filter: Filter): List<Option> {
        val selectedOptionList = ArrayList<Option>()
        val emptyPopularList = listOf<Option>()

        putSelectedOptionsToList(selectedOptionList, filter, emptyPopularList)

        return selectedOptionList
    }

    fun getSelectedAndPopularOptions(filter: Filter): List<Option> {
        val selectedOptionList = ArrayList<Option>()
        val popularOptionList: List<Option> = getPopularOptionList(filter)

        putSelectedOptionsToList(selectedOptionList, filter, popularOptionList)

        return selectedOptionList
    }

    private fun getPopularOptionList(filter: Filter): List<Option> {
        val checkedLevelOneOptions = ArrayList<Option>()
        val checkedLevelTwoOptions = ArrayList<Option>()
        val checkedLevelThreeOptions = ArrayList<Option>()

        for (option in filter.options) {
            addPopularOption(checkedLevelOneOptions, option)

            for (levelTwoCategory in option.levelTwoCategoryList) {
                addPopularOption(checkedLevelTwoOptions, levelTwoCategory.asOption())

                for (levelThreeCategory in levelTwoCategory.levelThreeCategoryList) {
                    addPopularOption(checkedLevelThreeOptions, levelThreeCategory.asOption())
                }
            }
        }

        return checkedLevelOneOptions + checkedLevelTwoOptions + checkedLevelThreeOptions
    }

    private fun addPopularOption(checkedOptions: ArrayList<Option>, option: Option) {
        if (option.isPopular) {
            checkedOptions.add(option)
        }
    }

    private fun putSelectedOptionsToList(selectedOptionList: MutableList<Option>, filter: Filter, popularOptionList: List<Option>) {
        if (isAddCategoryFilter(filter, popularOptionList)) {
            selectedOptionList.add(getSelectedCategoryAsOption(filter))
        } else {
            selectedOptionList.addAll(
                getCustomSelectedOptionList(filter) { option ->
                    if (popularOptionList.isEmpty()) {
                        getFilterViewState(option)
                    } else {
                        getFilterViewState(option) && !option.isPopular
                    }
                }
            )
        }

        selectedOptionList.addAll(popularOptionList)
    }

    private fun isAddCategoryFilter(filter: Filter, popularOptionList: List<Option>) : Boolean {
        return filter.isCategoryFilter && isCategorySelected() && !isSelectedCategoryInList(popularOptionList)
    }

    private fun isCategorySelected(): Boolean {
        return !TextUtils.isEmpty(getFilterValue(SearchApiConst.SC))
    }

    private fun isSelectedCategoryInList(optionList: List<Option>): Boolean {
        val selectedCategoryId = getFilterValue(SearchApiConst.SC)

        if (TextUtils.isEmpty(selectedCategoryId)) {
            return false
        }

        for (option in optionList) {
            if (selectedCategoryId == option.value) {
                return true
            }
        }

        return false
    }

    private fun getSelectedCategoryAsOption(filter: Filter): Option {
        val selectedCategoryId = getFilterValue(SearchApiConst.SC)
        val category = FilterHelper.getSelectedCategoryDetails(filter, selectedCategoryId)
        val selectedCategoryName = category?.categoryName ?: ""

        return OptionHelper.generateOptionFromCategory(selectedCategoryId, selectedCategoryName)
    }

    private fun getCustomSelectedOptionList(filter: Filter, isDisplayed:(Option) -> Boolean): List<Option> {
        val checkedOptions = ArrayList<Option>()

        for (option in filter.options) {
            if(isDisplayed(option)) {
                checkedOptions.add(option)
            }
        }

        return checkedOptions
    }

    fun isFilterActive() : Boolean {
        return filterViewState.isNotEmpty()
    }

    fun getFilterValue(key: String): String {
        val optionValues = mutableSetOf<String>()

        filterViewState.forEach { optionUniqueId ->
            if (OptionHelper.parseKeyFromUniqueId(optionUniqueId) == key) {
                val optionValue = OptionHelper.parseValueFromUniqueId(optionUniqueId)
                optionValues += optionValue.split(OptionHelper.VALUE_SEPARATOR).toSet()
            }
        }

        return optionValues.joinToString(separator = OptionHelper.VALUE_SEPARATOR)
    }

    fun getFilterViewState(option: Option) : Boolean {
        return getFilterViewState(option.uniqueId)
    }

    fun getFilterViewState(uniqueId: String) : Boolean {
        return filterViewState.contains(uniqueId)
    }

    fun getFilterCount() : Int {
        var filterCount = filterViewState.size

        if (hasMinAndMaxPriceFilter()) filterCount -= 1

        return filterCount
    }

    private fun hasMinAndMaxPriceFilter(): Boolean {
        var hasMinPriceFilter = false
        var hasMaxPriceFilter = false

        for(optionUniqueId in filterViewState) {
            val optionKey = OptionHelper.parseKeyFromUniqueId(optionUniqueId)
            if (optionKey == SearchApiConst.PMIN) hasMinPriceFilter = true
            if (optionKey == SearchApiConst.PMAX) hasMaxPriceFilter = true

            // Immediately return so it doesn't continue the loop
            if (hasMinPriceFilter && hasMaxPriceFilter) return true
        }

        return false
    }

    fun getParameter() : Map<String, String> {
        return getActiveFilterMap() + nonFilterParameter
    }

    fun getActiveFilterMap() : Map<String, String> {
        val filterParameter = mutableMapOf<String, String>()

        for(optionUniqueId in filterViewState) {
            val optionKey = OptionHelper.parseKeyFromUniqueId(optionUniqueId)
            val currentOptionValue = filterParameter[optionKey] ?: ""
            val addedOptionValue = OptionHelper.parseValueFromUniqueId(optionUniqueId)

            if (currentOptionValue == addedOptionValue) continue

            val optionSeparator = if (currentOptionValue.isNotEmpty())
                OptionHelper.OPTION_SEPARATOR
            else ""

            filterParameter[optionKey] = currentOptionValue + optionSeparator + addedOptionValue
        }

        return filterParameter
    }

    fun getActiveFilterOptionList() : List<Option> {
        val activeFilterOptionList = mutableListOf<Option>()

        filterViewState.forEach {
            activeFilterOptionList.add(OptionHelper.generateOptionFromUniqueId(it))
        }

        return activeFilterOptionList
    }

    @JvmOverloads
    fun setFilter(option: Option?, isFilterApplied: Boolean, isCleanUpExistingFilterWithSameKey: Boolean = false) {
        if(option == null) return

        if(isCleanUpExistingFilterWithSameKey) {
            cleanUpExistingFilterWithSameKey(option.key)
        }

        saveFilterViewState(option.uniqueId, isFilterApplied)
    }

    private fun cleanUpExistingFilterWithSameKey(newOptionKey: String) {
        val filterViewStateIterator = filterViewState.iterator()
        while(filterViewStateIterator.hasNext()) {
            val filterViewStateUniqueId = filterViewStateIterator.next()
            val existingOptionKey = OptionHelper.parseKeyFromUniqueId(filterViewStateUniqueId)
            if(existingOptionKey == newOptionKey) {
                filterViewStateIterator.remove()
            }
        }
    }

    private fun saveFilterViewState(uniqueId: String, isFilterApplied: Boolean) {
        if(isFilterApplied) {
            filterViewState.add(uniqueId)
        }
        else {
            filterViewState.remove(uniqueId)
        }
    }

    fun setFilter(optionList: List<Option>?) {
        if(optionList == null || optionList.isEmpty() ) return

        for(option in optionList) {
            val isFilterApplied = option.inputState.toBoolean()
            saveFilterViewState(option.uniqueId, isFilterApplied)
        }
    }
}