package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets

import com.tokopedia.graphql.data.model.GraphqlResponse
import rx.Subscriber

/**
 * Created by fwidjaja on 2019-05-14.
 */
class MapSearchLocationBottomSheetSubscriber(val view: MapSearchLocationBottomSheetView): Subscriber<GraphqlResponse>() {
    override fun onNext(t: GraphqlResponse?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
        view.hideListPointOfInterest()
    }
}