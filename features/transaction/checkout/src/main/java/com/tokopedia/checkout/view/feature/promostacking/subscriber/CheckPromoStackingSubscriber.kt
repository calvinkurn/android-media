package com.tokopedia.checkout.view.feature.promostacking.subscriber

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.promocheckout.common.domain.mapper.CheckPromoStackingCodeMapper
import rx.Subscriber

/**
 * Created by fwidjaja on 15/03/19.
 */
class CheckPromoStackingSubscriber(val mapper: CheckPromoStackingCodeMapper) : Subscriber<GraphqlResponse>() {
    // butuh param view, presenter -> cari tau view & presenter mana yg diperlukan untuk set applied
    override fun onNext(response: GraphqlResponse) {
        println("++ onNext")
        mapper.call(response)
    }

    override fun onCompleted() {
        println("++ onCompleted")
    }

    override fun onError(e: Throwable?) {
       println("++ onError = ${e.toString()}")
    }
}