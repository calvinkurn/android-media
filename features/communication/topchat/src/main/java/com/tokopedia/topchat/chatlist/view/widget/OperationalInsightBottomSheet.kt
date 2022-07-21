package com.tokopedia.topchat.chatlist.view.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.topchat.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class OperationalInsightBottomSheet: BottomSheetUnify() {

    private var childView: View? = null

    init {
        showKnob = true
        showCloseIcon = false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        childView = View.inflate(context, R.layout.bs_chat_operational_insight, null)
        setChild(childView)
    }
}