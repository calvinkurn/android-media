package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.play.widget.ui.model.PlayVideoWidgetUiModel

data class HomeRecommendationPlayWidgetUiModel(
    val playVideoWidgetUiModel: PlayVideoWidgetUiModel
): Visitable<HomeRecommendationTypeFactoryImpl> {
    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

}
