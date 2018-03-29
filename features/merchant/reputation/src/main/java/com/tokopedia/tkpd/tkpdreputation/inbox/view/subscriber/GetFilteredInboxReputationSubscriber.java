package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;

import rx.Subscriber;

/**
 * @author by nisie on 8/22/17.
 */

public class GetFilteredInboxReputationSubscriber extends GetFirstTimeInboxReputationSubscriber {

    public GetFilteredInboxReputationSubscriber(InboxReputation.View viewListener) {
        super(viewListener);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishRefresh();
        viewListener.onErrorGetFilteredInboxReputation(ErrorHandler.getErrorMessage(e));

    }

    @Override
    public void onNext(InboxReputationDomain inboxReputationDomain) {
        viewListener.finishRefresh();

        if (inboxReputationDomain.getInboxReputation().isEmpty()) {
            viewListener.onShowEmptyFilteredInboxReputation();
        } else {
            viewListener.onSuccessGetFilteredInboxReputation(mappingToViewModel(inboxReputationDomain));
        }
    }
}
