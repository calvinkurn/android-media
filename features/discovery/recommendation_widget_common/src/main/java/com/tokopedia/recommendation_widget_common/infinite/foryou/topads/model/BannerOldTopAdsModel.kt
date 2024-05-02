package com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.infinite.foryou.utils.TemporaryBackwardCompatible
import com.tokopedia.topads.sdk.domain.model.TopAdsImageUiModel

@TemporaryBackwardCompatible
class BannerOldTopAdsModel(
    val topAdsImageUiModel: TopAdsImageUiModel? = null,
    val position: Int = -1,
    val bannerType: String
) : ForYouRecommendationVisitable, ImpressHolder() {

    override fun type(typeFactory: ForYouRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return other is BannerOldTopAdsModel &&
            topAdsImageUiModel?.bannerId == other.topAdsImageUiModel?.bannerId
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return this == other
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BannerOldTopAdsModel

        if (topAdsImageUiModel != other.topAdsImageUiModel) return false

        return true
    }
}
