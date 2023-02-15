package com.tokopedia.topads.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topads.create.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class MpCreateGroupBudgetHelpSheet : BottomSheetUnify()  {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val contentView = View.inflate(context, R.layout.mp_create_group_budget_help_sheet, null)
        setChild(contentView)
        showKnob = false
        isHideable = true
        isDragable = false
        showCloseIcon = true
        setTitle(getString(R.string.daily_budget_tips))
    }
}
