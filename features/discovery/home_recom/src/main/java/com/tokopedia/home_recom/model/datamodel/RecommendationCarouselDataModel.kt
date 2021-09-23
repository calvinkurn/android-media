package com.tokopedia.home_recom.model.datamodel

import android.os.Bundle
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.view.adapter.HomeRecommendationTypeFactory
import com.tokopedia.home_recom.view.viewholder.RecommendationCarouselViewHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

/**
 * A Class of DataModel.
 *
 * This class for holding data for type factory pattern [RecommendationCarouselViewHolder]
 * @param title the title of widget recommendation carousel
 * @param products the list of recommendation item, it hold data for carousel
 */
data class RecommendationCarouselDataModel(
        val title: String,
        val appLinkSeeMore: String,
        val products: List<RecommendationCarouselItemDataModel>
) : HomeRecommendationDataModel {

    companion object {
        val LAYOUT = R.layout.fragment_recommendation_carousell
    }

    override fun type(typeFactory: HomeRecommendationTypeFactory): Int = typeFactory.type(this)

    fun contains(item: RecommendationItem) = products.any { it.productItem.productId == item.productId }

    fun contains(id: Long) = products.any { it.productItem.productId == id }

    override fun name(): String = title

    override fun equalsWith(newData: HomeRecommendationDataModel): Boolean {
        var isRecomItemSame = true
        if (products.size == (newData as RecommendationCarouselDataModel).products.size) {
            loop@ for (i in 0 until products.size) {
                if (!products[i].equalsWith(newData.products[i])) {
                    isRecomItemSame = false
                    break@loop
                }
            }
        }
        return if (newData is RecommendationCarouselDataModel) {
            title == newData.title &&
                    products.size == newData.products.size &&
                    products.hashCode() == newData.products.hashCode() &&
                    isRecomItemSame

        } else {
            false
        }
    }

    override fun newInstance(): HomeRecommendationDataModel {
        return RecommendationCarouselDataModel(
                title, appLinkSeeMore, products.toMutableList())
    }

    override fun getChangePayload(newData: HomeRecommendationDataModel): Bundle? = null

}