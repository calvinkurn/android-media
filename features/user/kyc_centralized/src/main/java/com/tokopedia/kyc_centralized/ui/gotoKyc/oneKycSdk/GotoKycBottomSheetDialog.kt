package com.tokopedia.kyc_centralized.ui.gotoKyc.oneKycSdk

import android.os.Bundle
import android.view.View
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.unifycomponents.BottomSheetUnify

class GotoKycBottomSheetDialog : BottomSheetUnify() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            showCloseIcon = it.getBoolean(SHOW_CLOSE_ICON).orFalse()
        }
        overlayClickDismiss = showCloseIcon
        clearContentPadding = true
    }

    fun setView(view: View) {
        setChild(view)
    }

    companion object {
        private const val SHOW_CLOSE_ICON = "SHOW_CLOSE_ICON"
        fun newInstance(showCloseIcon: Boolean) =
            GotoKycBottomSheetDialog().apply {
                arguments = Bundle().apply {
                    putBoolean(SHOW_CLOSE_ICON, showCloseIcon)
                }
            }

        const val TAG = "GOTO_KYC_BOTTOM_SHEET_DIALOG"
    }

}
