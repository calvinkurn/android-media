package com.tokopedia.contactus.inboxticket2.view.fragment

import android.content.Context
import android.view.View
import android.widget.TextView
import com.tokopedia.contactus.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class HelpFullBottomSheet(context: Context, private val closeBottomSheetListener: CloseSHelpFullBottomSheet) : BottomSheetUnify(), View.OnClickListener {
    init {
        val helpfullView = View.inflate(context, R.layout.helpfull_bottom_sheet_layout, null)
        val noButton = helpfullView.findViewById<TextView>(R.id.tv_no_button)
        val yesButton = helpfullView.findViewById<TextView>(R.id.tv_yes_button)
        noButton.setOnClickListener(this)
        yesButton.setOnClickListener(this)
        setChild(helpfullView)
        showKnob = true
        showCloseIcon = false
        showHeader = false
    }


    override fun onClick(view: View) {
        if (view.id == R.id.tv_no_button) {
            closeBottomSheetListener.onClick(false)
        } else if (view.id == R.id.tv_yes_button) {
            closeBottomSheetListener.onClick(true)
        }
    }

    interface CloseSHelpFullBottomSheet {
        fun onClick(agreed: Boolean)
    }
}