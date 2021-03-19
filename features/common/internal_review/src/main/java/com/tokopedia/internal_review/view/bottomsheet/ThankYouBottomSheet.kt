package com.tokopedia.internal_review.view.bottomsheet

import android.content.Context
import androidx.fragment.app.FragmentManager
import com.tokopedia.internal_review.R
import com.tokopedia.internal_review.analytics.ReviewTracking
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.internal_review.common.Const
import com.tokopedia.internal_review.factory.createReviewThankyouBottomSheet
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.sir_thank_you_bottom_sheet.view.*

/**
 * Created By @ilhamsuaib on 22/01/21
 */

class ThankYouBottomSheet(tracker: ReviewTracking,
                          userSession: UserSessionInterface) : BaseBottomSheet(tracker, userSession) {

    companion object {
        const val TAG = "SirThankYouBottomSheet"

        fun createInstance(context: Context): ThankYouBottomSheet {
            return createReviewThankyouBottomSheet(context)
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