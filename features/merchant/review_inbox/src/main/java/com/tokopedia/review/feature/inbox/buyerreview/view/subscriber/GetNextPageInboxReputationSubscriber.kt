package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.InboxReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation

/**
 * @author by nisie on 8/18/17.
 */
class GetNextPageInboxReputationSubscriber constructor(viewListener: InboxReputation.View?) :
    GetFirstTimeInboxReputationSubscriber(viewListener) {
    public override fun onCompleted() {}
    public override fun onError(e: Throwable) {
        viewListener!!.finishLoading()
        viewListener.onErrorGetNextPage(e)
    }

    public override fun onNext(inboxReputationDomain: InboxReputationDomain) {
        viewListener!!.finishLoading()
        viewListener.onSuccessGetNextPage(mappingToViewModel(inboxReputationDomain))
    }
}