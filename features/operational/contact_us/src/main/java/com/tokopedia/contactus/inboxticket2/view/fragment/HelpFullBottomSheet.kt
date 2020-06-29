package com.tokopedia.contactus.inboxticket2.view.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.contactus.R

class HelpFullBottomSheet(context: Context,
                          private val closeBottomSheetListener: CloseSHelpFullBottomSheet) : FrameLayout(context), View.OnClickListener {
    private fun init() {
        val helpfullView = LayoutInflater.from(context).inflate(R.layout.helpfull_bottom_sheet_layout, this, true)
        val noButton = helpfullView.findViewById<TextView>(R.id.tv_no_button)
        val yesButton = helpfullView.findViewById<TextView>(R.id.tv_yes_button)
        noButton.setOnClickListener(this)
        yesButton.setOnClickListener(this)
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

    init {
        init()
    }
}