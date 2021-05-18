package com.tokopedia.internal_review.view.bottomsheet

import androidx.fragment.app.FragmentManager
import com.tokopedia.internal_review.R
import com.tokopedia.internal_review.common.Const
import com.tokopedia.kotlin.extensions.view.loadImage
import kotlinx.android.synthetic.main.sir_thank_you_bottom_sheet.view.*

/**
 * Created By @ilhamsuaib on 22/01/21
 */

class ThankYouBottomSheet : BaseBottomSheet() {

    companion object {
        const val TAG = "SirThankYouBottomSheet"

        fun createInstance(): ThankYouBottomSheet {
            return ThankYouBottomSheet()
        }
    }

    override fun getResLayout(): Int = R.layout.sir_thank_you_bottom_sheet

    override fun setupView() = childView?.run {
        imgSirTankYou.loadImage(Const.IMG_THANK_YOU)
    }

    override fun show(fm: FragmentManager) {
        show(fm, TAG)
    }
}