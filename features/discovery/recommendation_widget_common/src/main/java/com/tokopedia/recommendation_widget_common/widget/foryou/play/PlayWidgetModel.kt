package com.tokopedia.recommendation_widget_common.widget.foryou.play

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.ui.model.PlayVideoWidgetUiModel
import com.tokopedia.recommendation_widget_common.widget.foryou.ForYouRecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.foryou.ForYouRecommendationVisitable

data class PlayWidgetModel(
    val cardId: String,
    val appLink: String,
    val playVideoWidgetUiModel: PlayVideoWidgetUiModel,
    val playVideoTrackerUiModel: PlayVideoTrackerUiModel
) : ForYouRecommendationVisitable, ImpressHolder() {

    override fun type(typeFactory: ForYouRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return other is PlayWidgetModel && other.playVideoWidgetUiModel.id == playVideoWidgetUiModel.id
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return this == other
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlayWidgetModel

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
