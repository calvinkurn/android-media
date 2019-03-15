package com.tokopedia.checkout.view.feature.promostacking.subscriber

import com.tokopedia.checkout.view.feature.cartlist.CartListPresenter
import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * Created by fwidjaja on 15/03/19.
 */
class CheckPromoStackingSubscriber (val presenter: CartListPresenter) : Subscriber<GraphqlResponse>() {
    override fun onNext(t: GraphqlResponse?) {
        println("++ onNext = ${t.toString()}")
    }

    override fun onCompleted() {
        println("++ onCompleted")
    }

    override fun onError(e: Throwable?) {
       println("++ onError = ${e.toString()}")
    }
}