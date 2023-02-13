package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationResponseWrapper
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation

/**
 * @author by nisie on 8/18/17.
 */
class RefreshInboxReputationSubscriber constructor(
    viewListener: InboxReputation.View,
    private val isUsingFilter: Boolean
) : GetFirstTimeInboxReputationSubscriber(viewListener) {

    override fun onError(e: Throwable) {
        viewListener.finishRefresh()
        viewListener.onErrorRefresh(e)
    }

    override fun onNext(inboxReputationResponse: InboxReputationResponseWrapper.Data.Response) {
        viewListener.finishRefresh()
        if (inboxReputationResponse.reputationList.isEmpty() && isUsingFilter) {
            viewListener.onShowEmptyFilteredInboxReputation()
        } else if (inboxReputationResponse.reputationList.isEmpty()) {
            viewListener.onShowEmpty()
        } else {
            viewListener.onSuccessRefresh(mappingToViewModel(inboxReputationResponse))
        }
    }
}