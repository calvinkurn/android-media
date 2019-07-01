package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.mapper.AutocompleteGeocodeMapper
import rx.Subscriber

/**
 * Created by fwidjaja on 2019-05-14.
 */
class AutocompleteGeocodeSubscriber(val view: AutocompleteBottomSheetListener,
                                    val mapper: AutocompleteGeocodeMapper) : Subscriber<GraphqlResponse>() {
    private val statusOK = "OK"

    override fun onNext(t: GraphqlResponse?) {
        val responseAutoCompleteGeocode = mapper.map(t)
        if (responseAutoCompleteGeocode.status.equals(statusOK, true)) {
            view.onSuccessGetAutocompleteGeocode(responseAutoCompleteGeocode.data)
        }
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
        view.hideListPointOfInterest()
    }
}