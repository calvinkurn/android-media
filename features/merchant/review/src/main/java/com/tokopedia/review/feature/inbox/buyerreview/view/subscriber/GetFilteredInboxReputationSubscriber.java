package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber;

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain;
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation;

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
        viewListener.onErrorGetFilteredInboxReputation(e);

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
