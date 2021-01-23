package com.tokopedia.sellerreview.view.bottomsheet

import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerreview.common.Const
import kotlinx.android.synthetic.main.sir_thank_you_bottom_sheet.view.*

/**
 * Created By @ilhamsuaib on 22/01/21
 */

class ThankYouBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "ThankYouBottomSheet"

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