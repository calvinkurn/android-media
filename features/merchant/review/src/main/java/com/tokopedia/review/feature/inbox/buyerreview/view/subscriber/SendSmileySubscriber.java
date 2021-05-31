package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber;

import com.tokopedia.review.common.util.ReviewErrorHandler;
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationDomain;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail;

import rx.Subscriber;

/**
 * @author by nisie on 8/31/17.
 */

public class SendSmileySubscriber extends Subscriber<SendSmileyReputationDomain> {


    private final InboxReputationDetail.View viewListener;
    private final String score;

    public SendSmileySubscriber(InboxReputationDetail.View viewListener, String score) {
        this.viewListener = viewListener;
        this.score = score;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoadingDialog();
        viewListener.onErrorSendSmiley(ReviewErrorHandler.getErrorMessage(viewListener.getContext(), e));
    }

    @Override
    public void onNext(SendSmileyReputationDomain sendSmileyReputationDomain) {
        viewListener.finishLoadingDialog();
        if (sendSmileyReputationDomain.isSuccess()) {
            viewListener.onSuccessSendSmiley(Integer.parseInt(score));
        } else {
            viewListener.onErrorSendSmiley(viewListener.getContext().getApplicationContext().getString(com.tokopedia.abstraction.R.string
                    .default_request_error_unknown));
        }
    }
}
