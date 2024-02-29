package com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.infinite.foryou.utils.TemporaryBackwardCompatible
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

@TemporaryBackwardCompatible
class BannerOldTopAdsModel(
    val topAdsImageViewModel: TopAdsImageViewModel? = null,
    val position: Int = -1,
    val bannerType: String
) : ForYouRecommendationVisitable, ImpressHolder() {

    override fun type(typeFactory: ForYouRecommendationTypeFactory) = Int.MIN_VALUE

    override fun areItemsTheSame(other: Any): Boolean {
        return other is BannerOldTopAdsModel &&
            topAdsImageViewModel?.bannerId == other.topAdsImageViewModel?.bannerId
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return this == other
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BannerOldTopAdsModel

        if (topAdsImageViewModel != other.topAdsImageViewModel) return false

        return true
    }
}
