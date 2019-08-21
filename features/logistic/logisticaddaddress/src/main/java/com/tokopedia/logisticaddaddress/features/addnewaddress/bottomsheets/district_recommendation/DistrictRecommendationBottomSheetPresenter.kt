package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.district_recommendation

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.logisticaddaddress.domain.mapper.DistrictRecommendationMapper
import com.tokopedia.logisticaddaddress.domain.usecase.DistrictRecommendationUseCase
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteGeocodeSubscriber
import com.tokopedia.usecase.RequestParams
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-31.
 */
class DistrictRecommendationBottomSheetPresenter @Inject constructor(private val districtRecommendationUseCase: DistrictRecommendationUseCase,
                                                                     private val districtRecommendationMapper: DistrictRecommendationMapper)
    : BaseDaggerPresenter<DistrictRecommendationBottomSheetListener>() {

    fun getDistrictRecommendation(keyQuery: String, numPage: String) {
        districtRecommendationUseCase.setParams(keyQuery, numPage)
        districtRecommendationUseCase.execute(RequestParams.create(), DistrictRecommendationSubscriber(view, districtRecommendationMapper, numPage))
    }

    override fun detachView() {
        super.detachView()
        districtRecommendationUseCase.unsubscribe()
    }

    fun clearCacheDistrictRecommendation() {
        districtRecommendationUseCase.clearCache()
    }
}