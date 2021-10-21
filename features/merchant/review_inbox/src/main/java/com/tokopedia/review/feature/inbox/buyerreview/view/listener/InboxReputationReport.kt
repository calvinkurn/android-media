package com.tokopedia.review.feature.inbox.buyerreview.view.listener

import android.content.Context
import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter

/**
 * @author by nisie on 9/13/17.
 */
interface InboxReputationReport {
    interface View : CustomerView {
        fun showLoadingProgress()
        fun onErrorReportReview(errorMessage: String?)
        fun onSuccessReportReview()
        fun removeLoadingProgress()
        val context: Context
    }

    interface Presenter : CustomerPresenter<View?> {
        fun reportReview(
            reviewId: String,
            shopId: String,
            checkedRadioButtonId: Int,
            otherReason: String
        )
    }
}