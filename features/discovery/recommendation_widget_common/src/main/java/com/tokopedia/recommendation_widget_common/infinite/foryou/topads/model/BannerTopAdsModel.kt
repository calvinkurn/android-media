package com.tokopedia.recommendation_widget_common.infinite.foryou.topads.model

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAppLog
import com.tokopedia.topads.sdk.domain.model.TopAdsImageViewModel

data class BannerTopAdsModel(
    val topAdsImageViewModel: TopAdsImageViewModel? = null,
    val cardId: String,
    val layoutCard: String,
    val layoutItem: String,
    val categoryId: String,
    val position: Int = -1,
    val pageName: String,
    val tabIndex: Int = -1,
    val tabName: String = "",
    val appLog: RecommendationAppLog = RecommendationAppLog(),
) : ForYouRecommendationVisitable, ImpressHolder() {

    override fun type(typeFactory: ForYouRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return other is BannerTopAdsModel &&
            topAdsImageViewModel?.bannerId == other.topAdsImageViewModel?.bannerId
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return other == this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BannerTopAdsModel

        if (topAdsImageViewModel != other.topAdsImageViewModel) return false

        return true
    }
}
