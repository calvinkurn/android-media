package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
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

    override fun onNext(inboxReputationDomain: InboxReputationDomain) {
        viewListener.finishRefresh()
        if (inboxReputationDomain.inboxReputation.isNullOrEmpty() && isUsingFilter) {
            viewListener.onShowEmptyFilteredInboxReputation()
        } else if (inboxReputationDomain.inboxReputation.isNullOrEmpty() && !isUsingFilter) {
            viewListener.onShowEmpty()
        } else {
            viewListener.onSuccessRefresh(mappingToViewModel(inboxReputationDomain))
        }
    }
}