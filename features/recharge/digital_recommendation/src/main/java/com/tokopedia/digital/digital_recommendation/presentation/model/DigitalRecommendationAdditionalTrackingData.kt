package com.tokopedia.digital.digital_recommendation.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author by furqan on 24/09/2021
 */
@Parcelize
data class DigitalRecommendationAdditionalTrackingData(
        var userType: String = "",
        var widgetPosition: String = "",
        var pgCategories: List<Int> = emptyList(),
        var dgCategories: List<Int> = emptyList()
) : Parcelable