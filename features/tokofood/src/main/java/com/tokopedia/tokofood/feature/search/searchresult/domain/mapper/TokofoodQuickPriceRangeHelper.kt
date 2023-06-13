package com.tokopedia.tokofood.feature.search.searchresult.domain.mapper

import com.tokopedia.filter.common.data.Option
import com.tokopedia.kotlin.extensions.view.orZero
import javax.inject.Inject

class TokofoodQuickPriceRangeHelper @Inject constructor() {

    fun getAppliedCount(optionList: List<Option>?): Int {
        return optionList?.count { it.inputState.toBoolean() }.orZero()
    }

    fun getIsOptionsSameAsInitial(
        options: List<Option>,
        initialOptions: List<Option>?
    ): Boolean {
        return isOptionsSizeSameAsInitial(
            options,
            initialOptions
        ) && isSelectedOptionsSameAsInitial(options, initialOptions)
    }

    private fun isOptionsSizeSameAsInitial(
        options: List<Option>,
        initialOptions: List<Option>?
    ): Boolean {
        val appliedCount = getAppliedCount(options)
        return appliedCount == getInitialAppliedCount(initialOptions)
    }

    private fun getInitialAppliedCount(initialOptions: List<Option>?): Int {
        return getAppliedCount(initialOptions)
    }

    private fun isSelectedOptionsSameAsInitial(
        options: List<Option>,
        initialOptions: List<Option>?
    ): Boolean {
        options.forEach { option ->
            if (initialOptions?.any { it.value == option.value && it.inputState != option.inputState } == true) {
                return false
            }
        }
        return true
    }

}