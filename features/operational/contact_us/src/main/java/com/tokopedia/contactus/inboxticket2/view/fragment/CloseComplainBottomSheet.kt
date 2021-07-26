package com.tokopedia.contactus.inboxticket2.view.fragment

import android.content.Context
import android.view.View
import android.widget.TextView
import com.tokopedia.contactus.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class CloseComplainBottomSheet(context: Context,
                               private val closeBottomSheetListener: CloseComplainBottomSheetListner) : BottomSheetUnify(), View.OnClickListener {

    init {
        val closeComplainView = View.inflate(context, R.layout.close_complain_bottom_sheet_layout, null)
        val noButton = closeComplainView.findViewById<TextView>(R.id.tv_no_button)
        val yesButton = closeComplainView.findViewById<TextView>(R.id.tv_yes_button)
        noButton.setOnClickListener(this)
        yesButton.setOnClickListener(this)

        setChild(closeComplainView)
        showKnob = true
        showCloseIcon = false
        showHeader = false
    }

    override fun onClick(view: View) {
        if (view.id == R.id.tv_no_button) {
            closeBottomSheetListener.onClickComplain(false)
        } else if (view.id == R.id.tv_yes_button) {
            closeBottomSheetListener.onClickComplain(true)
        }
    }

    interface CloseComplainBottomSheetListner {
        fun onClickComplain(agreed: Boolean)
    }
}