package com.tokopedia.tkpd.tkpdreputation.inbox.view.subscriber;

import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.InboxReputationItemDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ReputationBadgeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.ReputationDataDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.RevieweeBadgeCustomerDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.RevieweeBadgeSellerDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.listener.InboxReputationDetail;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationItemViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.InboxReputationViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.ReputationDataViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ReputationBadgeViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.RevieweeBadgeCustomerViewModel;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.RevieweeBadgeSellerViewModel;

import java.util.ArrayList;
import java.util.List;

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
        viewListener.onErrorRefreshInboxDetail(ErrorHandler.getErrorMessage(e));
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
