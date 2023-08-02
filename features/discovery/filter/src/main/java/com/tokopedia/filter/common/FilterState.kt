package com.tokopedia.filter.common

import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option
import com.tokopedia.filter.common.helper.equalsIgnoreOrder
import com.tokopedia.filter.newdynamicfilter.helper.FilterHelper.optionList
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper
import com.tokopedia.filter.newdynamicfilter.helper.OptionHelper.hasKey

/**
 * FilterState is immutable variant of FilterController.
 */
data class FilterState(
    private val nonFilterParameter: Map<String, String> = mapOf(),
    private val filterList: List<Filter> = listOf(),
    val filterViewState: Set<String> = setOf(),
) {

    val isFilterActive: Boolean
        get() = filterViewState.isNotEmpty()

    val activeFilterOptionList: List<Option>
        get() = filterViewState.map(OptionHelper::generateOptionFromUniqueId)

    val activeFilterMap: Map<String, String>
        get() = OptionHelper.toMap(activeFilterOptionList)

    val parameter: Map<String, String>
        get() = nonFilterParameter + activeFilterMap

    fun from(
        parameter: Map<String, String> = this.parameter,
        filterList: List<Filter> = this.filterList,
    ): FilterState {
        val optionList = filterList.optionList()
        val nonFilterParameter = parameter.filter { (key, _) -> !optionList.hasKey(key) }

        return copy(
            nonFilterParameter = nonFilterParameter,
            filterList = filterList,
            filterViewState = filterViewState(parameter, nonFilterParameter, optionList)
        )
    }

    private fun filterViewState(
        parameter: Map<String, String>,
        nonFilterParameter: Map<String, String>,
        optionList: List<Option>,
    ): MutableSet<String> {
        val filterParameter = parameter - nonFilterParameter.keys

        val optionListForFilterViewState = mutableListOf<Option>()
        optionList.forEach { option ->
            val valueInParameter = filterParameter[option.key] ?: return@forEach

            val isFreeTextOption = option.isTypeTextBox || option.value.isEmpty()
            if (isFreeTextOption)
                optionListForFilterViewState.add(option.clone().apply { value = valueInParameter })
            else if (isOptionSelected(option, valueInParameter))
                optionListForFilterViewState.add(option)
            else
                option.levelTwoCategoryList.forEach { levelTwoCategory ->
                    if (levelTwoCategory.value == valueInParameter)
                        optionListForFilterViewState.add(levelTwoCategory.asOption())
                    else
                        levelTwoCategory.levelThreeCategoryList.forEach { levelThreeCategory ->
                            if (levelThreeCategory.value == valueInParameter)
                                optionListForFilterViewState.add(levelThreeCategory.asOption())
                        }
                }
        }

        return optionListForFilterViewState.mapTo(mutableSetOf()) { it.uniqueId }
    }

    private fun isOptionSelected(
        option: Option,
        valueInParameter: String,
    ): Boolean {
        val optionValues = option.value.split(OptionHelper.VALUE_SEPARATOR)
        val optionValuesInParameter = valueInParameter.split(OptionHelper.OPTION_SEPARATOR)
        optionValuesInParameter.forEach { optionValue ->
            val filterParameterValues = optionValue.split(OptionHelper.VALUE_SEPARATOR)

            if (filterParameterValues.equalsIgnoreOrder(optionValues)) return true
        }

        return false
    }

    fun appendFilterList(filterList: List<Filter>): FilterState = from(
        filterList = this.filterList + filterList
    )

    fun setFilter(
        optionToApply: Option,
        isFilterApplied: Boolean,
        isCleanUpFilterWithSameKey: Boolean = false,
    ): FilterState = copy(
        filterViewState = filterViewState.run {
            val cleanUpFilterViewState = if (isCleanUpFilterWithSameKey) filter {
                OptionHelper.parseKeyFromUniqueId(it) != optionToApply.key
            } else this

            if (isFilterApplied) (cleanUpFilterViewState + optionToApply.uniqueId).toSet()
            else (cleanUpFilterViewState - optionToApply.uniqueId).toSet()
        }
    )

    fun setFilter(optionList: List<Option>): FilterState = copy(
        filterViewState = filterViewState.run {
            val filtersToApply = optionList
                .filter { it.inputState.toBoolean() }
                .map { it.uniqueId }

            val filtersToUnApply = optionList
                .filter { it.inputState.toBoolean().not() }
                .map { it.uniqueId }

            this + filtersToApply - filtersToUnApply.toSet()
        }
    )

    fun isFilterApplied(option: Option?): Boolean = isFilterApplied(option?.uniqueId)

    fun isFilterApplied(uniqueId: String?): Boolean = filterViewState.contains(uniqueId ?: "")

    fun resetFilters(): FilterState = copy(filterViewState = emptySet())
}
