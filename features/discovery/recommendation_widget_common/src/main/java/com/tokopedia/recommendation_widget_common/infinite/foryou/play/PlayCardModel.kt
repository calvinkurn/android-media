package com.tokopedia.recommendation_widget_common.infinite.foryou.play

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.ui.model.PlayVideoWidgetUiModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.infinite.foryou.ForYouRecommendationVisitable
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationAppLog

data class PlayCardModel(
    val cardId: String,
    val appLink: String,
    val playVideoWidgetUiModel: PlayVideoWidgetUiModel,
    val playVideoTrackerUiModel: PlayVideoTrackerUiModel,
    val isAds: Boolean,
    val shopId: String,
    val pageName: String,
    val position: Int,
    val appLog: RecommendationAppLog = RecommendationAppLog(),
    val tabIndex: Int = -1,
    val tabName: String = ""
) : ForYouRecommendationVisitable, ImpressHolder() {

    override fun type(typeFactory: ForYouRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return other is PlayCardModel && other.playVideoWidgetUiModel.id == playVideoWidgetUiModel.id
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return this == other
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayCardModel

        if (playVideoWidgetUiModel != other.playVideoWidgetUiModel) return false

        return true
    }

    data class PlayVideoTrackerUiModel(
        val videoType: String,
        val partnerId: String,
        val recommendationType: String,
        val playChannelId: String, // equal contentOriginID
        val layoutCard: String,
        val layoutItem: String,
        val categoryId: String
    )
}
