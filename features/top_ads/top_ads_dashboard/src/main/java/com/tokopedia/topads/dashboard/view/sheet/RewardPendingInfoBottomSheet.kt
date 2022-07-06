package com.tokopedia.topads.dashboard.view.sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class RewardPendingInfoBottomSheet : BottomSheetUnify() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        setUpChildView()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUpChildView() {
        setChild(View.inflate(context, R.layout.topads_reward_pending_info_bottom_sheet,null))
        context?.resources?.getString(R.string.topads_credit_reward_pending)?.let { setTitle(it) }
        isFullpage = false
        showCloseIcon = true
    }
}