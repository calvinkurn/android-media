package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationResponseWrapper
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation

/**
 * @author by nisie on 8/18/17.
 */
class GetNextPageInboxReputationSubscriber constructor(viewListener: InboxReputation.View) :
    GetFirstTimeInboxReputationSubscriber(viewListener) {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        viewListener.finishLoading()
        viewListener.onErrorGetNextPage(e)
    }

    override fun onNext(inboxReputationResponse: InboxReputationResponseWrapper.Data.Response) {
        viewListener.finishLoading()
        viewListener.onSuccessGetNextPage(mappingToViewModel(inboxReputationResponse))
    }
}