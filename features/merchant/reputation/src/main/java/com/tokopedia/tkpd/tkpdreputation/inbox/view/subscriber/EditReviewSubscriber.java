package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationForm;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ShareModel;

import rx.Subscriber;

/**
 * @author by nisie on 9/12/17.
 */

public class EditReviewSubscriber extends Subscriber<SendReviewDomain> {
    private final InboxReputationForm.View viewListener;
    private final boolean shareFb;
    private final ShareModel shareModel;

    public EditReviewSubscriber(InboxReputationForm.View viewListener,
                                boolean shareFb, ShareModel shareModel) {
        this.viewListener = viewListener;
        this.shareFb = shareFb;
        this.shareModel = shareModel;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoadingProgress();
        viewListener.onErrorEditReview(ErrorHandler.getErrorMessage(viewListener.getActivity().getApplicationContext(), e));
    }

    @Override
    public void onNext(SendReviewDomain sendReviewDomain) {
        viewListener.finishLoadingProgress();
        if (sendReviewDomain.isSuccess() && shareFb) {
            viewListener.onSuccessEditReviewWithShareFb(shareModel);
        } else if (sendReviewDomain.isSuccess())
            viewListener.onSuccessEditReview();
        else
            viewListener.onErrorSendReview(viewListener.getActivity().getApplicationContext().getString(R.string
                    .default_request_error_unknown));
    }
}
