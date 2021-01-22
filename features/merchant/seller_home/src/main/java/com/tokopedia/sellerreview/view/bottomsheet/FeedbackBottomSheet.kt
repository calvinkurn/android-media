package com.tokopedia.sellerreview.view.bottomsheet

import androidx.fragment.app.FragmentManager
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerreview.common.Const
import kotlinx.android.synthetic.main.sir_feedback_bottom_sheet.view.*

/**
 * Created By @ilhamsuaib on 22/01/21
 */

class FeedbackBottomSheet : BaseBottomSheet() {

    companion object {
        private const val TAG = "FeedbackBottomSheet"

        fun createInstance(): FeedbackBottomSheet {
            return FeedbackBottomSheet().apply {
                overlayClickDismiss = false
            }
        }
    }

    private var onSubmitted: (() -> Unit)? = null

    override fun getResLayout(): Int = R.layout.sir_feedback_bottom_sheet

    override fun setupView() = childView?.run {
        imgSirFeedback.setImageUrl(Const.IMG_REQUEST_FEEDBACK)
    }

    override fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    fun setOnSubmittedListener(action: () -> Unit): FeedbackBottomSheet {
        onSubmitted = action
        return this
    }
}