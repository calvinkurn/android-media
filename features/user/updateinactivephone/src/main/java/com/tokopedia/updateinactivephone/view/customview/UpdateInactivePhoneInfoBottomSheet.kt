package com.tokopedia.updateinactivephone.view.customview

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View

import com.tokopedia.design.bottomsheet.BottomSheetView
import com.tokopedia.updateinactivephone.R

class UpdateInactivePhoneInfoBottomSheet(context: Context) : BottomSheetView(context) {

    override fun getLayout(): Int {
        return R.layout.bottom_sheet_info_layout
    }

    override fun init(context: Context) {
        layoutInflater = if (context is Activity)
            context.layoutInflater
        else
            ((context as ContextWrapper).baseContext as Activity).layoutInflater

        bottomSheetView = layoutInflater.inflate(layout, null)
        setContentView(bottomSheetView)

        bottomSheetView.findViewById<View>(R.id.button_cancel).setOnClickListener { dismiss() }
    }
}
