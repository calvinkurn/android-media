package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.InboxReputationResponseWrapper
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputation

/**
 * @author by nisie on 8/22/17.
 */
class GetFilteredInboxReputationSubscriber constructor(viewListener: InboxReputation.View) :
    GetFirstTimeInboxReputationSubscriber(viewListener) {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        viewListener.finishRefresh()
        viewListener.onErrorGetFilteredInboxReputation(e)
    }

    override fun onNext(inboxReputationResponse: InboxReputationResponseWrapper.Data.Response) {
        viewListener.finishRefresh()
        if (inboxReputationResponse.reputationList.isEmpty()) {
            viewListener.onShowEmptyFilteredInboxReputation()
        } else {
            viewListener.onSuccessGetFilteredInboxReputation(
                mappingToViewModel(
                    inboxReputationResponse
                )
            )
        }
    }
}