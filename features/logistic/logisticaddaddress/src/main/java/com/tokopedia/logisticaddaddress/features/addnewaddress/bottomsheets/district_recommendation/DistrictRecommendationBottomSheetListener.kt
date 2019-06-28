package com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.district_recommendation

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_recommendation.DistrictRecommendationResponseUiModel

/**
 * Created by fwidjaja on 2019-05-31.
 */
interface DistrictRecommendationBottomSheetListener : CustomerView {
    fun onSuccessGetDistrictRecommendation(getDistrictRecommendationResponseUiModel: DistrictRecommendationResponseUiModel, numPage: String)
}