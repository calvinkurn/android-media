package com.tokopedia.topads.common.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topads.common.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class CreateGroupBudgetHelpSheet : BottomSheetUnify()  {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val contentView = View.inflate(context, R.layout.topads_common_create_group_budget_help_sheet, null)
        setChild(contentView)
        showKnob = false
        isHideable = true
        isDragable = false
        showCloseIcon = true
        setTitle(getString(R.string.topads_common_daily_budget_tips))
    }
}
