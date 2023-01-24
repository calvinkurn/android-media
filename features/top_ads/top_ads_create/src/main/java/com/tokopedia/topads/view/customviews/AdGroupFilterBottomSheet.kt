package com.tokopedia.topads.view.customviews

import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.common.data.Option

class AdGroupFilterBottomSheet(private val callback:((option: Option,isChecked: Boolean) -> Unit)?=null) : FilterGeneralDetailBottomSheet() {
    override fun onOptionClick(option: Option, isChecked: Boolean, position: Int) {
        callback?.invoke(option, isChecked)
        super.onOptionClick(option, isChecked, position)
    }
}
