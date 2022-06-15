package com.tokopedia.thankyou_native.recommendationdigital.presentation.view

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage

interface IDigitalRecommendationView {

    fun loadRecommendation(
        fragment: BaseDaggerFragment,
        pgCategoryIds: List<Int>,
        pageType: DigitalRecommendationPage
    )

}