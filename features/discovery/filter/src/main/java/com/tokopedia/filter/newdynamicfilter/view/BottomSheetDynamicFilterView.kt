package com.tokopedia.filter.newdynamicfilter.view

import com.tokopedia.filter.common.data.Option

interface BottomSheetDynamicFilterView : DynamicFilterView {
    fun isSelectedCategory(option: Option): Boolean
    fun selectCategory(option: Option, filterTitle: String)
    fun saveCheckedState(option: Option, isChecked: Boolean, filterTitle: String)
    fun removeSelectedOption(option: Option, filterTitle: String)
}
