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
        val title: String = "",
        val seeMoreAppLink: String = "",
        val pageName: String = "",
        val widgetParam: String = "",
        val recommendationItemList: List<RecommendationItem> = listOf(),
        val productCardModelList: List<ProductCardModel> = listOf(),
        val height: Int = 0
) : RecommendationVisitable{
    override fun visitableId(): String? {
        return pageName
    }

    override fun equalsWith(b: Any?): Boolean {
        return b is BestSellerDataModel && b.title == title &&
                b.pageName == pageName &&
                b.recommendationItemList.containsAll(b.recommendationItemList)
    }

    override fun getChangePayloadFrom(b: Any?): Bundle? = Bundle()

    override fun type(typeFactory: RecommendationTypeFactory): Int = typeFactory.type(this)
}