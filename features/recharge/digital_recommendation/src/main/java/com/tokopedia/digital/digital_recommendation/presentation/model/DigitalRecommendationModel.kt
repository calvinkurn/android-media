package com.tokopedia.digital.digital_recommendation.presentation.model

import android.os.Parcelable
import com.tokopedia.recharge_component.digital_card.presentation.model.DigitalUnifyModel
import kotlinx.parcelize.Parcelize

/**
 * @author by furqan on 17/09/2021
 */
@Parcelize
data class DigitalRecommendationModel(
        val userType: String,
        val title: String,
        val items: List<DigitalRecommendationItemUnifyModel>
) : Parcelable

@Parcelize
data class DigitalRecommendationItemUnifyModel(
        val unify: DigitalUnifyModel,
        val tracking: DigitalRecommendationTrackingModel = DigitalRecommendationTrackingModel()
): Parcelable

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