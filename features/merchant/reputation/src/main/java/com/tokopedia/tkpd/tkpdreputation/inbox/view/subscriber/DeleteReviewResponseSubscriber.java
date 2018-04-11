package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;

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
        viewListener.onErrorDeleteReviewResponse(ErrorHandler.getErrorMessage(e));
    }

    @Override
    public void onNext(DeleteReviewResponseDomain deleteReviewResponseDomain) {
        viewListener.finishLoadingDialog();
        if (deleteReviewResponseDomain.isSuccess())
            viewListener.onSuccessDeleteReviewResponse();
        else
            viewListener.onErrorDeleteReviewResponse(
                    MainApplication.getAppContext().getString(R.string.default_request_error_unknown)
            );

    }
}
