package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationDetailDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail

/**
 * @author by nisie on 9/4/17.
 */
class RefreshInboxReputationDetailSubscriber constructor(viewListener: InboxReputationDetail.View?) :
    GetInboxReputationDetailSubscriber(viewListener) {
    public override fun onError(e: Throwable) {
        viewListener!!.finishRefresh()
        viewListener.onErrorRefreshInboxDetail(e)
    }

    public override fun onNext(inboxReputationDetailDomain: InboxReputationDetailDomain) {
        viewListener!!.finishRefresh()
        viewListener.onSuccessRefreshGetInboxDetail(
            convertToReputationViewModel(inboxReputationDetailDomain.getInboxReputationDomain()).getList()
                .get(0),
            mappingToListItemViewModel(inboxReputationDetailDomain.getReviewDomain())
        )
    }
}