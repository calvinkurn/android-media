package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber;

import com.tokopedia.review.common.util.ReviewErrorHandler;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendReplyReviewDomain;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail;

import rx.Subscriber;

/**
 * @author by nisie on 9/28/17.
 */

public class ReplyReviewSubscriber extends Subscriber<SendReplyReviewDomain> {
    private final InboxReputationDetail.View viewListener;

    public ReplyReviewSubscriber(InboxReputationDetail.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoadingDialog();
        viewListener.onErrorReplyReview(ReviewErrorHandler.getErrorMessage(viewListener.getContext().getApplicationContext(), e));
    }

    @Override
    public void onNext(SendReplyReviewDomain sendReplyReviewDomain) {
        viewListener.finishLoadingDialog();
        if (sendReplyReviewDomain.isSuccess())
            viewListener.onSuccessReplyReview();
        else
            viewListener.onErrorReplyReview(
                    viewListener.getContext().getApplicationContext().getString(com.tokopedia.abstraction.R.string
                            .default_request_error_unknown));

    }
}
