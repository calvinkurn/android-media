package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import android.os.Bundle
import com.tokopedia.home.beranda.domain.gql.feed.Product
import com.tokopedia.home.beranda.domain.gql.recommendationcard.RecommendationCard
import com.tokopedia.home.beranda.presentation.view.adapter.HomeRecommendationVisitable
import com.tokopedia.home.beranda.presentation.view.adapter.factory.homeRecommendation.HomeRecommendationTypeFactory
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.smart_recycler_helper.SmartVisitable

data class HomeRecommendationItemDataModel(
    val product: Product? = null,
    val recommendationCard: RecommendationCard,
    val productCardModel: ProductCardModel,
    val pageName: String = "",
    val layoutName: String = "",
    val position: Int = -1
) : HomeRecommendationVisitable, ImpressHolder() {
    override fun type(typeFactory: HomeRecommendationTypeFactory): Int {
        return typeFactory.type(this)
    }

    override fun equalsDataModel(dataModel: SmartVisitable<*>): Boolean {
        return dataModel == this
    }

    override fun getUniqueIdentity(): Any {
        return recommendationCard?.id.orEmpty()
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        return Bundle()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HomeRecommendationItemDataModel

        if (recommendationCard != other.recommendationCard) return false

        return true
    }

    override fun hashCode(): Int {
        return recommendationCard.hashCode()
    }
}
