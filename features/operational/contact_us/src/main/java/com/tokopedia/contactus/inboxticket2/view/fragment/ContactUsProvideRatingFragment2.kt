package com.tokopedia.contactus.inboxticket2.view.fragment

import android.os.Bundle
import android.view.View
import com.tokopedia.contactus.R
import com.tokopedia.unifycomponents.BottomSheetUnify

class ContactUsProvideRatingFragment2 : BottomSheetUnify() {

    init {
        val helpfullView = View.inflate(context, R.layout.helpfull_bottom_sheet_layout, null)
//        noButton.setOnClickListener(this)
//        yesButton.setOnClickListener(this)
        setChild(helpfullView)
        showKnob = true
        showCloseIcon = false
        showHeader = false
    }

    companion object {
        fun newInstance(bundle: Bundle?): ContactUsProvideRatingFragment2 {
            val fragment = ContactUsProvideRatingFragment2()
            fragment.arguments = bundle
            return fragment
        }
    }
}
