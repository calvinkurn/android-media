package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.mapper.AutocompleteMapper
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetListener
import rx.Subscriber

/**
 * Created by fwidjaja on 2019-05-20.
 */
class AutocompleteSubscriber(val view: AutocompleteBottomSheetListener,
                             val mapper: AutocompleteMapper): Subscriber<GraphqlResponse>() {

    override fun onNext(t: GraphqlResponse?) {
        val responseAutoCompleteGeocode = mapper.map(t)
        view.onSuccessGetAutocomplete(responseAutoCompleteGeocode.data)
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
        view.hideListPointOfInterest()
    }
}