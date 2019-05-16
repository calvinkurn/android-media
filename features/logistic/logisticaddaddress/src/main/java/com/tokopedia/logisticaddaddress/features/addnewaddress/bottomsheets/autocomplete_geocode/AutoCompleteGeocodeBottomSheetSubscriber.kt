package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.mapper.AutoCompleteGeocodeMapper
import rx.Subscriber

/**
 * Created by fwidjaja on 2019-05-14.
 */
class AutoCompleteGeocodeBottomSheetSubscriber(val view: AutoCompleteGeocodeBottomSheetView,
                                               val mapper: AutoCompleteGeocodeMapper): Subscriber<GraphqlResponse>() {
    private val statusOK = "OK"

    override fun onNext(t: GraphqlResponse?) {
        val responseAutoCompleteGeocode = mapper.map(t)
        if (responseAutoCompleteGeocode.status.equals(statusOK, true)) {
            view.onSuccessGetListPointOfInterest(responseAutoCompleteGeocode.data)
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
        view.hideListPointOfInterest()
    }
}