package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.abstraction.R
import com.tokopedia.review.common.util.ReviewErrorHandler.getErrorMessage
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendReplyReviewDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import rx.Subscriber

/**
 * @author by nisie on 9/28/17.
 */
class ReplyReviewSubscriber constructor(private val viewListener: InboxReputationDetail.View?) :
    Subscriber<SendReplyReviewDomain?>() {
    public override fun onCompleted() {}
    public override fun onError(e: Throwable) {
        viewListener!!.finishLoadingDialog()
        viewListener.onErrorReplyReview(
            getErrorMessage(
                viewListener.getContext().getApplicationContext(), e
            )
        )
    }

    public override fun onNext(sendReplyReviewDomain: SendReplyReviewDomain) {
        viewListener!!.finishLoadingDialog()
        if (sendReplyReviewDomain.isSuccess()) viewListener.onSuccessReplyReview() else viewListener.onErrorReplyReview(
            viewListener.getContext().getApplicationContext()
                .getString(R.string.default_request_error_unknown)
        )
    }
}