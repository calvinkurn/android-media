package com.tokopedia.sellerreview.view.bottomsheet

import androidx.fragment.app.FragmentManager
import com.tokopedia.sellerhome.R

/**
 * Created By @ilhamsuaib on 22/01/21
 */

class ThankYouBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "ThankYouBottomSheet"

        fun createInstance(): ThankYouBottomSheet {
            return ThankYouBottomSheet().apply {
                overlayClickDismiss = false
            }
        }
    }

    override fun getResLayout(): Int = R.layout.sir_thank_you_bottom_sheet

    override fun setupView() = childView?.run {

    }

    override fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}