package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.sendreview.SkipReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationForm;

import rx.Subscriber;

/**
 * @author by nisie on 9/12/17.
 */

public class SkipReviewSubscriber extends Subscriber<SkipReviewDomain> {
    private final InboxReputationForm.View viewListener;

    public SkipReviewSubscriber(InboxReputationForm.View viewListener) {
        this.viewListener = viewListener;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoadingProgress();
        viewListener.onErrorSkipReview(ErrorHandler.getErrorMessage(viewListener.getActivity().getApplicationContext(), e));
    }

    @Override
    public void onNext(SkipReviewDomain skipReviewDomain) {
        viewListener.finishLoadingProgress();
        if (skipReviewDomain.isSuccess()) {
            viewListener.onSuccessSkipReview();
        } else {
            viewListener.onErrorSkipReview(viewListener.getActivity().getApplicationContext()
                    .getString(R.string.default_request_error_unknown));
        }

    }
}
