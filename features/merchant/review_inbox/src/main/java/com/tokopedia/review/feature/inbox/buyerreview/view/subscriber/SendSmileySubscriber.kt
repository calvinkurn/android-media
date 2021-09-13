package com.tokopedia.review.feature.inbox.buyerreview.view.subscriber

import com.tokopedia.abstraction.R
import com.tokopedia.review.common.util.ReviewErrorHandler.getErrorMessage
import com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail.SendSmileyReputationDomain
import com.tokopedia.review.feature.inbox.buyerreview.view.listener.InboxReputationDetail
import rx.Subscriber

/**
 * @author by nisie on 8/31/17.
 */
class SendSmileySubscriber constructor(
    private val viewListener: InboxReputationDetail.View?,
    private val score: String?
) : Subscriber<SendSmileyReputationDomain?>() {
    public override fun onCompleted() {}
    public override fun onError(e: Throwable) {
        viewListener!!.finishLoadingDialog()
        viewListener.onErrorSendSmiley(getErrorMessage(viewListener.getContext(), e))
    }

    public override fun onNext(sendSmileyReputationDomain: SendSmileyReputationDomain) {
        viewListener!!.finishLoadingDialog()
        if (sendSmileyReputationDomain.isSuccess()) {
            viewListener.onSuccessSendSmiley(score!!.toInt())
        } else {
            viewListener.onErrorSendSmiley(
                viewListener.getContext().getApplicationContext().getString(
                    R.string.default_request_error_unknown
                )
            )
        }
    }
}