package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import android.content.Context;

import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SendReviewValidateDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationForm;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ShareModel;

import rx.Subscriber;

/**
 * @author by nisie on 9/12/17.
 */

public class EditReviewWithoutImageSubscriber extends Subscriber<SendReviewValidateDomain> {
    private final InboxReputationForm.View viewListener;
    private final boolean shareFb;
    private final ShareModel shareModel;
    private final Context context;

    public EditReviewWithoutImageSubscriber(InboxReputationForm.View viewListener,
                                            boolean shareFb, ShareModel shareModel) {
        this.viewListener = viewListener;
        this.shareFb = shareFb;
        this.shareModel = shareModel;
        this.context = viewListener.getActivity().getApplicationContext();
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoadingProgress();
        viewListener.onErrorEditReview(ErrorHandler.getErrorMessage(context, e));
    }

    @Override
    public void onNext(SendReviewValidateDomain sendReviewValidateDomain) {
        viewListener.finishLoadingProgress();
        if (sendReviewValidateDomain.getIsSuccess() == 1 && shareFb) {
            viewListener.onSuccessEditReviewWithShareFb(shareModel);
        } else if (sendReviewValidateDomain.getIsSuccess() == 1) {
            viewListener.onSuccessEditReview();
        } else {
            viewListener.onErrorEditReview(context.getString(R.string
                    .default_request_error_unknown));

        }
    }
}
