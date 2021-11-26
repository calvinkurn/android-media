package com.tokopedia.digital.digital_recommendation.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationAdditionalTrackingData
import com.tokopedia.digital.digital_recommendation.presentation.model.DigitalRecommendationPage

/**
 * @author by furqan on 22/09/2021
 */
class DigitalRecommendationData(
        val viewModelFactory: ViewModelProvider.Factory,
        val lifecycleOwner: LifecycleOwner,
        var additionalTrackingData: DigitalRecommendationAdditionalTrackingData,
        val page: DigitalRecommendationPage
)