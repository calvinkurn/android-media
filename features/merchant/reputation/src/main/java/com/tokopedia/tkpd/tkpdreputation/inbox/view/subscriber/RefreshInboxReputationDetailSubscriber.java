package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;

/**
 * @author by nisie on 9/4/17.
 */

public class RefreshInboxReputationDetailSubscriber extends GetInboxReputationDetailSubscriber {
    public RefreshInboxReputationDetailSubscriber(InboxReputationDetail.View viewListener) {
        super(viewListener);

    }

    @Override
    public void onError(Throwable e) {
        viewListener.finishRefresh();
        viewListener.onErrorRefreshInboxDetail(e);
    }

    @Override
    public void onNext(InboxReputationDetailDomain inboxReputationDetailDomain) {
        viewListener.finishRefresh();
        viewListener.onSuccessRefreshGetInboxDetail(
                convertToReputationViewModel(inboxReputationDetailDomain.getInboxReputationDomain
                        ()).getList().get(0),
                mappingToListItemViewModel(inboxReputationDetailDomain.getReviewDomain())
        );
    }

}
