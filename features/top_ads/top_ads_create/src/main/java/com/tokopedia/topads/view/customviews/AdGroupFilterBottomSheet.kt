package com.tokopedia.topads.view.customviews

import android.os.Bundle
import android.view.View
import com.tokopedia.filter.bottomsheet.filtergeneraldetail.FilterGeneralDetailBottomSheet
import com.tokopedia.filter.common.data.Option

class AdGroupFilterBottomSheet(private val callback:((option: Option,isChecked: Boolean) -> Unit)?=null) : FilterGeneralDetailBottomSheet() {
    override fun onOptionClick(option: Option, isChecked: Boolean, position: Int) {
        super.onOptionClick(option, isChecked, position)
        callback?.invoke(option, isChecked)
        clearAction()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        clearAction()
        super.onViewCreated(view, savedInstanceState)
    }
}
