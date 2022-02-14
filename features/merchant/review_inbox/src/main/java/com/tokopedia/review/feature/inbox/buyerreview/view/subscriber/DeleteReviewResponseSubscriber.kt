package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.DeleteReviewResponseDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import rx.Subscriber

/**
 * @author by nisie on 9/27/17.
 */
class DeleteReviewResponseSubscriber constructor(private val viewListener: InboxReputationDetail.View) :
    Subscriber<DeleteReviewResponseDomain>() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        viewListener.finishLoadingDialog()
        viewListener.onErrorDeleteReviewResponse(viewListener.getErrorMessage())
    }

    override fun onNext(deleteReviewResponseDomain: DeleteReviewResponseDomain) {
        viewListener.finishLoadingDialog()
        if (deleteReviewResponseDomain.isSuccess) viewListener.onSuccessDeleteReviewResponse() else viewListener.onErrorDeleteReviewResponse(
            viewListener.getErrorMessage()
        )
    }
}