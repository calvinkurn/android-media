package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendReplyReviewDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import rx.Subscriber

/**
 * @author by nisie on 9/28/17.
 */
class ReplyReviewSubscriber constructor(private val viewListener: InboxReputationDetail.View) :
    Subscriber<SendReplyReviewDomain>() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        viewListener.finishLoadingDialog()
        viewListener.onErrorReplyReview(viewListener.getErrorMessage())
    }

    override fun onNext(sendReplyReviewDomain: SendReplyReviewDomain) {
        viewListener.finishLoadingDialog()
        if (sendReplyReviewDomain.isSuccess) viewListener.onSuccessReplyReview() else viewListener.onErrorReplyReview(
            viewListener.getErrorMessage()
        )
    }
}