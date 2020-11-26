package com.tokopedia.paylater.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.paylater.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.toDp

class PayLaterSignupBottomSheet: BottomSheetUnify() {

    init {
        setShowListener {
            bottomSheet.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(view: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss()
                    }
                }
            })
        }
    }

    private val childLayoutRes = R.layout.paylater_signup_bottomsheet_widget

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDefaultParams()
        val childView = LayoutInflater.from(context).inflate(childLayoutRes,
                null, false)
        setChild(childView)
    }

    private fun setDefaultParams() {
        isDragable = true
        isHideable = true
        showCloseIcon = true
        showHeader = true
        customPeekHeight = (getScreenHeight() / 2).toDp()
    }

    companion object {
        private val DIALOG_TITLE = R.string.paylater_signup_title

        fun newInstance()= PayLaterSignupBottomSheet()
    }
}