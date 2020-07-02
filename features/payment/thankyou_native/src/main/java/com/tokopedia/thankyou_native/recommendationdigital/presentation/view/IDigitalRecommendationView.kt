package com.tokopedia.thankyou_native.recommendationdigital.presentation.view

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.trackingoptimizer.TrackingQueue

interface IDigitalRecommendationView {
    fun loadRecommendation(fragment: BaseDaggerFragment, trackingQueue: TrackingQueue)

}