package com.tokopedia.tokopedianow.searchcategory.presentation.listener

import com.tokopedia.filter.common.data.Option

interface CategoryFilterListener {

    fun onCategoryFilterChipClick(option: Option, isSelected: Boolean)
}