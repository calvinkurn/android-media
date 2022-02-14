package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationDetailDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.InboxReputationItemUiModel

/**
 * @author by nisie on 9/4/17.
 */
class RefreshInboxReputationDetailSubscriber constructor(viewListener: InboxReputationDetail.View) :
    GetInboxReputationDetailSubscriber(viewListener) {

    override fun onError(e: Throwable) {
        viewListener.finishRefresh()
        viewListener.onErrorRefreshInboxDetail(e)
    }

    override fun onNext(inboxReputationDetailDomain: InboxReputationDetailDomain) {
        viewListener.finishRefresh()
        viewListener.onSuccessRefreshGetInboxDetail(
            convertToReputationViewModel(inboxReputationDetailDomain.inboxReputationDomain).list.getOrNull(0) ?: InboxReputationItemUiModel(),
            mappingToListItemViewModel(inboxReputationDetailDomain.reviewDomain)
        )
    }
}