package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactoryImpl
import com.tokopedia.kotlin.model.ImpressHolder

data class RecomEntityCardUiModel(
    val id: String,
    val layoutCard: String, // layout
    val layoutItem: String, // layoutTracker
    val categoryId: String,
    val title: String,
    val subTitle: String,
    val appLink: String,
    val imageUrl: String,
    val backgroundColor: List<String>,
    val labelState: LabelState
) : Visitable<HomeRecommendationTypeFactoryImpl>, ImpressHolder() {
    data class LabelState(
        val iconUrl: String,
        val title: String,
        val textColor: String
    )

    override fun type(typeFactory: HomeRecommendationTypeFactoryImpl): Int {
        return typeFactory.type(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RecomEntityCardUiModel

        if (id != other.id) return false
        if (layoutCard != other.layoutCard) return false
        if (imageUrl != other.imageUrl) return false
        if (layoutItem != other.layoutItem) return false
        if (categoryId != other.categoryId) return false
        if (title != other.title) return false
        if (subTitle != other.subTitle) return false
        if (appLink != other.appLink) return false
        if (labelState != other.labelState) return false
        if (backgroundColor != backgroundColor) return false

        return true
    }
}
