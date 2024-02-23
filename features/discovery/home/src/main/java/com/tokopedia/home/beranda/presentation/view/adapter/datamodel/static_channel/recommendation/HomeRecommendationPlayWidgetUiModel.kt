package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.ui.model.PlayVideoWidgetUiModel

data class HomeRecommendationPlayWidgetUiModel(
    val cardId: String,
    val appLink: String,
    val playVideoWidgetUiModel: PlayVideoWidgetUiModel,
    val playVideoTrackerUiModel: HomeRecommendationPlayVideoTrackerUiModel,
    val isAds: Boolean,
    val shopId: String,
    val pageName: String
) : BaseHomeRecommendationVisitable, ImpressHolder() {

    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun areItemsTheSame(other: Any): Boolean {
        return other is HomeRecommendationPlayWidgetUiModel && other.playVideoWidgetUiModel.id == playVideoWidgetUiModel.id
    }

    override fun areContentsTheSame(other: Any): Boolean {
        return this == other
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HomeRecommendationPlayWidgetUiModel

        if (playVideoWidgetUiModel != other.playVideoWidgetUiModel) return false

        return true
    }

    data class HomeRecommendationPlayVideoTrackerUiModel(
        val videoType: String,
        val partnerId: String,
        val recommendationType: String,
        val playChannelId: String, // equal contentOriginID
        val layoutCard: String,
        val layoutItem: String,
        val categoryId: String
    )
}
