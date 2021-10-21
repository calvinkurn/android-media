package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.abstraction.R
import com.tokopedia.review.common.util.ReviewErrorHandler.getErrorMessage
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
        viewListener.onErrorDeleteReviewResponse(getErrorMessage(viewListener.context, e))
    }

    override fun onNext(deleteReviewResponseDomain: DeleteReviewResponseDomain) {
        viewListener.finishLoadingDialog()
        if (deleteReviewResponseDomain.isSuccess) viewListener.onSuccessDeleteReviewResponse() else viewListener.onErrorDeleteReviewResponse(
            viewListener.context.applicationContext
                .getString(R.string.default_request_error_unknown)
        )
    }
}