package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputation;


/**
 * @author by nisie on 8/18/17.
 */

public class GetNextPageInboxReputationSubscriber extends GetFirstTimeInboxReputationSubscriber {

    public GetNextPageInboxReputationSubscriber(InboxReputation.View viewListener) {
        super(viewListener);
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishLoading();
        viewListener.onErrorGetNextPage(e);
    }

    @Override
    public void onNext(InboxReputationDomain inboxReputationDomain) {
        viewListener.finishLoading();
        viewListener.onSuccessGetNextPage(mappingToViewModel(inboxReputationDomain));
    }
}
