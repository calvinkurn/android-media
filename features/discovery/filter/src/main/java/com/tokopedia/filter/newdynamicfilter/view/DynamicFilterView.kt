package com.tokopedia.filter.newdynamicfilter.view

import com.tokopedia.filter.common.data.Filter
import com.tokopedia.filter.common.data.Option

interface DynamicFilterView {
    fun onExpandableItemClicked(filter: Filter)

    fun loadLastCheckedState(option: Option): Boolean
    fun saveCheckedState(option: Option, isChecked: Boolean)

    fun removeSavedTextInput(uniqueId: String)
    fun saveTextInput(uniqueId: String, textInput: String)

    fun getSelectedOptions(filter: Filter): List<Option>
    fun removeSelectedOption(option: Option)

    fun onPriceSliderRelease(minValue: Int, maxValue: Int)
    fun onPriceSliderPressed(minValue: Int, maxValue: Int)

    fun onPriceEditedFromTextInput(minValue: Int, maxValue: Int)

    fun getFilterValue(key: String): String
    fun getFilterViewState(uniqueId: String): Boolean

    fun onPriceRangeClicked()
}
