package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapListener
import rx.Subscriber

/**
 * Created by fwidjaja on 2019-05-16.
 */
class GetDistrictSubscriber(val view: PinpointMapListener,
                            val mapper: GetDistrictMapper): Subscriber<GraphqlResponse>() {

    override fun onNext(t: GraphqlResponse?) {
        val getDistrictResponseUiModel = mapper.map(t)
        view.onSuccessPlaceGetDistrict(getDistrictResponseUiModel.data)
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
    }
}