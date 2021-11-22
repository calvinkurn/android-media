package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import rx.Subscriber

/**
 * @author by nisie on 8/31/17.
 */
class SendSmileySubscriber constructor(
    private val viewListener: InboxReputationDetail.View,
    private val score: String
) : Subscriber<SendSmileyReputationDomain>() {

    override fun onCompleted() {}

    override fun onError(e: Throwable) {
        viewListener.finishLoadingDialog()
        viewListener.onErrorSendSmiley(viewListener.getErrorMessage())
    }

    override fun onNext(sendSmileyReputationDomain: SendSmileyReputationDomain) {
        viewListener.finishLoadingDialog()
        if (sendSmileyReputationDomain.isSuccess) {
            viewListener.onSuccessSendSmiley(score.toIntOrZero())
        } else {
            viewListener.onErrorSendSmiley(viewListener.getErrorMessage())
        }
    }
}