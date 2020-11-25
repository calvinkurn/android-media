package com.tokopedia.thankyou_native.recommendationdigital.presentation.view

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.trackingoptimizer.TrackingQueue

interface IDigitalRecommendationView {
    fun loadRecommendation(thanksPageData: ThanksPageData,
                           fragment: BaseDaggerFragment, trackingQueue: TrackingQueue?)

}