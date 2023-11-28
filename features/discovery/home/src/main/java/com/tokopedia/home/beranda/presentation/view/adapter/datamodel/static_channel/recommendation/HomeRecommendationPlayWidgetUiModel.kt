package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.ui.model.PlayVideoWidgetUiModel

data class HomeRecommendationPlayWidgetUiModel(
    val cardId: String,
    val appLink: String,
    val playVideoWidgetUiModel: PlayVideoWidgetUiModel,
    val playVideoTrackerUiModel: HomeRecommendationPlayVideoTrackerUiModel
) : Visitable<HomeRecommendationTypeFactoryImpl>, ImpressHolder() {
    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
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
