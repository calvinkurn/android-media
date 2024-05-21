@file:SuppressLint("EntityFieldAnnotation")

package com.tokopedia.recommendation_widget_common.infinite.foryou.content

import android.annotation.SuppressLint
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAppLog

data class ContentCardModel(
    val id: String,
    val layoutCard: String, // layout
    val layoutItem: String, // layoutTracker
    val categoryId: String,
    val title: String,
    val subTitle: String,
    val appLink: String,
    val imageUrl: String,
    val backgroundColor: List<String>,
    val labelState: LabelState,
    val position: Int,
    val isAds: Boolean,
    val shopId: String,
    val pageName: String,
    val appLog: RecommendationAppLog = RecommendationAppLog(),
    val tabIndex: Int = -1,
    val tabName: String = ""
) : ImpressHolder(), ForYouRecommendationVisitable {

    data class LabelState(
        val iconUrl: String,
        val title: String,
        val textColor: String
    )

    override fun areItemsTheSame(other: Any): Boolean {
        return other is ContentCardModel && other.id == id
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return other == this
    }

    override fun type(typeFactory: ForYouRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }
}
