package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.get_district

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.mapper.AutoCompleteGeocodeMapper
import com.tokopedia.logisticaddaddress.domain.mapper.GetDistrictMapper
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutoCompleteGeocodeBottomSheetView
import rx.Subscriber

/**
 * Created by fwidjaja on 2019-05-16.
 */
class GetDistrictBottomSheetSubscriber(val view: GetDistrictBottomSheetView,
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