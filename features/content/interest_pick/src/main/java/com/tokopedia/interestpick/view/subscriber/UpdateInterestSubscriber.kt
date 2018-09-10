package com.tokopedia.interestpick.view.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.interestpick.view.listener.InterestPickContract
import rx.Subscriber

/**
 * @author by milhamj on 10/09/18.
 */
class UpdateInterestSubscriber(val view: InterestPickContract.View): Subscriber<GraphqlResponse>() {
    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        view.hideLoading()
    }

    override fun onNext(t: GraphqlResponse?) {
        view.hideLoading()
    }
}