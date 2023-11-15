package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.play.widget.ui.model.PlayVideoWidgetUiModel

data class HomeRecommendationPlayWidgetUiModel(
    val appLink: String,
    val playVideoWidgetUiModel: PlayVideoWidgetUiModel,
    val playVideoTrackerUiModel: HomeRecommendationPlayVideoTrackerUiModel
) : Visitable<HomeRecommendationTypeFactoryImpl>, ImpressHolder() {
    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
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
