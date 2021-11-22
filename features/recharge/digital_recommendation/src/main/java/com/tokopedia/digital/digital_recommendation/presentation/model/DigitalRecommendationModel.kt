package com.tokopedia.digital.digital_recommendation.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author by furqan on 17/09/2021
 */
@Parcelize
data class DigitalRecommendationModel(
        val userType: String,
        val title: String,
        val items: List<DigitalRecommendationItemModel>
) : Parcelable

@Parcelize
data class DigitalRecommendationItemModel(
        val iconUrl: String,
        val categoryName: String,
        val productName: String,
        val applink: String,
        val tracking: DigitalRecommendationTrackingModel = DigitalRecommendationTrackingModel(),
        val type: DigitalRecommendationType,
        val price: String,
        val beforePrice: String,
        val discountTag: String
) : Parcelable