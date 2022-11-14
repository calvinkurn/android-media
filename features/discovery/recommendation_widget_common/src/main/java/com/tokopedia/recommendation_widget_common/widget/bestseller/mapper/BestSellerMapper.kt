package com.tokopedia.recommendation_widget_common.widget.bestseller.mapper

import android.content.Context
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.utils.getMaxHeightForGridView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.bestseller.model.BestSellerDataModel
import com.tokopedia.unifycomponents.CardUnify2
import kotlinx.coroutines.Dispatchers

/**
 * Created by Lukas on 05/11/20.
 */
class BestSellerMapper (
    private val context: Context
){
    suspend fun mappingRecommendationWidget(
        recommendationWidget: RecommendationWidget,
        cardInteraction: Boolean = false,
        currentModel: BestSellerDataModel = BestSellerDataModel()
    ): BestSellerDataModel{
        val productList = mappingProductCards(recommendationWidget.recommendationItemList, cardInteraction)
        return BestSellerDataModel(
            channelId = recommendationWidget.channelId,
            title = recommendationWidget.title,
            subtitle = recommendationWidget.subtitle,
            height = getMaxHeightProductCards(productList),
            pageName = recommendationWidget.pageName,
            productCardModelList = productList,
            recommendationItemList = recommendationWidget.recommendationItemList,
            filterChip = recommendationWidget.recommendationFilterChips,
            seeMoreAppLink = recommendationWidget.seeMoreAppLink,
            dividerType = currentModel.dividerType,
            dividerSize = currentModel.dividerSize
        )
    }

    private fun mappingProductCards(recommendationList: List<RecommendationItem>, cardInteraction: Boolean = false): List<ProductCardModel> {
        return recommendationList.map { recommendationItem ->
            recommendationItem.toProductCardModel(hasThreeDots = true, cardInteraction = cardInteraction)
        }
    }

    suspend fun getMaxHeightProductCards(productCardModels: List<ProductCardModel>): Int{
        return productCardModels.getMaxHeightForGridView(
            context = context,
            coroutineDispatcher = Dispatchers.IO,
            productImageWidth = context.resources.getDimensionPixelSize(R.dimen.product_card_carousel_item_width)
        )
    }
}
