package com.tokopedia.filter.bottomsheet.filter

internal interface FilterViewListener {

    fun onOptionClick(filterViewModel: FilterViewModel, optionViewModel: OptionViewModel)

    fun onSeeAllOptionClick(filterViewModel: FilterViewModel)
}