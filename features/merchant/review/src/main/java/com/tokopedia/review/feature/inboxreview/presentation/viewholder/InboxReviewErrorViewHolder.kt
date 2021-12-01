package com.tokopedia.review.feature.inboxreview.presentation.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.review.R
import com.tokopedia.review.databinding.GlobalErrorInboxReviewBinding
import com.tokopedia.review.feature.inboxreview.presentation.adapter.GlobalErrorStateListener
import com.tokopedia.review.feature.inboxreview.presentation.model.InboxReviewErrorUiModel

class InboxReviewErrorViewHolder(
    view: View,
    private val globalErrorStateListener: GlobalErrorStateListener
) : AbstractViewHolder<InboxReviewErrorUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.global_error_inbox_review
    }

    private val binding = GlobalErrorInboxReviewBinding.bind(view)

    override fun bind(element: InboxReviewErrorUiModel) {
        with(binding) {
            globalErrorInboxReview.apply {
                setType(GlobalError.SERVER_ERROR)
                setActionClickListener {
                    globalErrorStateListener.onActionGlobalErrorStateClicked()
                }
            }
        }
    }
}