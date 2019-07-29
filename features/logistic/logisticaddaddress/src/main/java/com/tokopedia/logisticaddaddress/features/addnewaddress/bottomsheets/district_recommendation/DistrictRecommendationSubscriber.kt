package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.district_recommendation

import com.tokopedia.graphql.data.model.GraphqlResponse
import com.tokopedia.logisticaddaddress.domain.mapper.AutocompleteGeocodeMapper
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetListener
import rx.Subscriber

/**
 * Created by fwidjaja on 2019-05-31.
 */
class DistrictRecommendationSubscriber(val view: DistrictRecommendationBottomSheetListener,
                                       val mapper: DistrictRecommendationMapper,
                                       val numPage: String) : Subscriber<GraphqlResponse>() {

    override fun onNext(t: GraphqlResponse?) {
        val districtRecommendationResponseUiModel = mapper.map(t)
        view.onSuccessGetDistrictRecommendation(districtRecommendationResponseUiModel, numPage)
    }

    override fun onCompleted() {

    }

    override fun onError(e: Throwable?) {
        e?.printStackTrace()
        // view.hideListPointOfInterest()
    }
}