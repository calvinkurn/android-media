package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.abstraction.R
import com.tokopedia.review.common.util.ReviewErrorHandler.getErrorMessage
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.DeleteReviewResponseDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import rx.Subscriber

/**
 * @author by nisie on 9/27/17.
 */
class DeleteReviewResponseSubscriber constructor(private val viewListener: InboxReputationDetail.View?) :
    Subscriber<DeleteReviewResponseDomain?>() {
    public override fun onCompleted() {}
    public override fun onError(e: Throwable) {
        viewListener!!.finishLoadingDialog()
        viewListener.onErrorDeleteReviewResponse(getErrorMessage(viewListener.getContext(), e))
    }

    public override fun onNext(deleteReviewResponseDomain: DeleteReviewResponseDomain) {
        viewListener!!.finishLoadingDialog()
        if (deleteReviewResponseDomain.isSuccess()) viewListener.onSuccessDeleteReviewResponse() else viewListener.onErrorDeleteReviewResponse(
            viewListener.getContext().getApplicationContext()
                .getString(R.string.default_request_error_unknown)
        )
    }
}