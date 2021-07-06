package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber;

import com.tokopedia.review.common.util.ReviewErrorHandler;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail;

import rx.Subscriber;

/**
 * @author by nisie on 9/27/17.
 */

public class DeleteReviewResponseSubscriber extends Subscriber<DeleteReviewResponseDomain> {
    private final InboxReputationDetail.View viewListener;

    public DeleteReviewResponseSubscriber(InboxReputationDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoadingDialog();
        viewListener.onErrorDeleteReviewResponse(ReviewErrorHandler.getErrorMessage(viewListener.getContext(), e));
    }

    @Override
    public void onNext(DeleteReviewResponseDomain deleteReviewResponseDomain) {
        viewListener.finishLoadingDialog();
        if (deleteReviewResponseDomain.isSuccess())
            viewListener.onSuccessDeleteReviewResponse();
        else
            viewListener.onErrorDeleteReviewResponse(
                    viewListener.getContext().getApplicationContext().getString(com.tokopedia.abstraction.R.string.default_request_error_unknown)
            );

    }
}
