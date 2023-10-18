package com.tokopedia.recommendation_widget_common.widget.bestseller.mapper

import android.content.Context
import com.tokopedia.home_component_header.model.ChannelHeader
import com.tokopedia.home_component_header.util.HomeChannelHeaderRollenceController
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
        return currentModel.copy(
            channelId = recommendationWidget.channelId,
            title = recommendationWidget.title,
            subtitle = recommendationWidget.subtitle,
            height = getMaxHeightProductCards(productList),
            pageName = recommendationWidget.pageName,
            productCardModelList = productList,
            recommendationItemList = recommendationWidget.recommendationItemList,
            filterChip = recommendationWidget.recommendationFilterChips,
            seeMoreAppLink = recommendationWidget.seeMoreAppLink,
            channelHeader = ChannelHeader(
                channelId = recommendationWidget.channelId,
                name = recommendationWidget.title,
                subtitle = recommendationWidget.subtitle,
                applink = recommendationWidget.seeMoreAppLink,
                headerType = getHeaderType(),
            )
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

    companion object {
        fun getHeaderType(): ChannelHeader.HeaderType {
            return if(HomeChannelHeaderRollenceController.isHeaderUsingRollenceVariant()) {
                ChannelHeader.HeaderType.REVAMP
            } else ChannelHeader.HeaderType.CONTROL
        }
    }
}
