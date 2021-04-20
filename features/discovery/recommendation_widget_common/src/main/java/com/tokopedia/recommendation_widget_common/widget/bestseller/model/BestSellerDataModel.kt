package com.tokopedia.recommendation_widget_common.widget.bestseller.model

import android.os.Bundle
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.data.RecommendationFilterChipsEntity
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationTypeFactory
import com.tokopedia.recommendation_widget_common.widget.bestseller.factory.RecommendationVisitable

/**
 * Created by Lukas on 05/11/20.
 */
data class BestSellerDataModel(
        val filterChip: List<RecommendationFilterChipsEntity.RecommendationFilterChip> = listOf(),
        val id: String = "",
        val title: String = "",
        val subtitle: String = "",
        val seeMoreAppLink: String = "",
        val pageName: String = "",
        val widgetParam: String = "",
        val recommendationItemList: List<RecommendationItem> = listOf(),
        val productCardModelList: List<ProductCardModel> = listOf(),
        val height: Int = 0
) : RecommendationVisitable{
    override fun visitableId(): String? {
        return id
    }

    override fun equalsWith(b: Any?): Boolean {
        return b is BestSellerDataModel && b.title == title &&
                b.pageName == pageName &&
                b.seeMoreAppLink == seeMoreAppLink &&
                b.recommendationItemList === recommendationItemList &&
                b.recommendationItemList.containsAll(recommendationItemList)
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? {
        if(b is BestSellerDataModel){
            if(!b.recommendationItemList.containsAll(recommendationItemList) || (recommendationItemList.isEmpty() && b.recommendationItemList.isNotEmpty())){
                return Bundle().apply { putBoolean(BEST_SELLER_UPDATE_RECOMMENDATION, true) }
            } else if(b.recommendationItemList !== recommendationItemList){
                return Bundle().apply { putBoolean(BEST_SELLER_HIDE_LOADING_RECOMMENDATION, true) }
            }
        }

        return null
    }

    override fun type(typeFactory: RecommendationTypeFactory): Int = typeFactory.type(this)

    companion object{
        const val BEST_SELLER_UPDATE_RECOMMENDATION = "UPDATE_RECOMMENDATION"
        const val BEST_SELLER_HIDE_LOADING_RECOMMENDATION = "HIDE_LOADING_RECOMMENDATION"
    }
}