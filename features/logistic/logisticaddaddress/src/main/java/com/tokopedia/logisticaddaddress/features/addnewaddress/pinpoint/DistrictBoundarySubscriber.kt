package com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictBoundaryMapper
import rx.Subscriber

/**
 * Created by fwidjaja on 2019-06-10.
 */
class DistrictBoundarySubscriber(val view: PinpointMapListener,
                                 val mapper: DistrictBoundaryMapper) : Subscriber<GraphqlResponse>() {
    override fun onNext(t: GraphqlResponse?) {
        val districtBoundaryResponseUiModel = mapper.map(t)
        view.onSuccessGetDistrictBoundary(districtBoundaryResponseUiModel.geometry)
    }

    override fun onCompleted() {
    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
    }
}