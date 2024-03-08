package com.tokopedia.recommendation_widget_common.infinite.foryou.banner

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.infinite.foryou.utils.TemporaryBackwardCompatible

@TemporaryBackwardCompatible
data class BannerRecommendationModel(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val url: String,
    val applink: String,
    val buAttribution: String,
    val creativeName: String,
    val target: String,
    val position: Int,
    val galaxyAttribution: String,
    val affinityLabel: String,
    val shopId: String,
    val categoryPersona: String,
    val tabName: String
) : ImpressHolder(), ForYouRecommendationVisitable {

    override fun areItemsTheSame(other: Any): Boolean {
        return other is BannerRecommendationModel && id == other.id
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return this == other
    }

    override fun type(typeFactory: ForYouRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }
}
