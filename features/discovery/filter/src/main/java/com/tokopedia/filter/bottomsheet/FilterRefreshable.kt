package com.tokopedia.filter.bottomsheet

import com.tokopedia.filter.bottomsheet.filter.OptionViewModel
import com.tokopedia.filter.common.data.Filter

internal interface FilterRefreshable {
    val willSortOptionList: Boolean
    val filter: Filter
    var optionViewModelList: MutableList<OptionViewModel>

    fun resetAndReturnShouldUpdate(): Boolean {
        var shouldUpdate = false

        optionViewModelList.forEach {
            if (it.isSelected) {
                shouldUpdate = true
                it.isSelected = false
                it.option.inputState = false.toString()
            }
        }
        return shouldUpdate
    }
}
