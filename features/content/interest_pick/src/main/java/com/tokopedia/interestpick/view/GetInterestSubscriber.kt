package com.tokopedia.interestpick.view

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.interestpick.view.listener.InterestPickContract
import rx.Subscriber

/**
 * @author by milhamj on 07/09/18.
 */
class GetInterestSubscriber(val listener: InterestPickContract.View)
    : Subscriber<GraphqlResponse>() {

    override fun onNext(t: GraphqlResponse?) {
    }

    override fun onError(e: Throwable?) {
    }

    override fun onCompleted() {
    }

}