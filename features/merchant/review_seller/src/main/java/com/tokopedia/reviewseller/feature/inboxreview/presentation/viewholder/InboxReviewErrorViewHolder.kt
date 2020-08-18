package com.tokopedia.reviewseller.feature.inboxreview.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.reviewseller.R
import com.tokopedia.reviewseller.feature.inboxreview.presentation.adapter.GlobalErrorStateListener
import com.tokopedia.reviewseller.feature.inboxreview.presentation.model.InboxReviewErrorUiModel
import kotlinx.android.synthetic.main.global_error_inbox_review.view.*

class InboxReviewErrorViewHolder(view: View,
                                private val globalErrorStateListener: GlobalErrorStateListener): AbstractViewHolder<InboxReviewErrorUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.global_error_inbox_review
    }

    override fun bind(element: InboxReviewErrorUiModel) {
        with(itemView) {
            globalError_inboxReview?.setType(GlobalError.SERVER_ERROR)
            globalError_inboxReview?.setActionClickListener {
                globalErrorStateListener.onActionGlobalErrorStateClicked()
            }
        }
    }
}