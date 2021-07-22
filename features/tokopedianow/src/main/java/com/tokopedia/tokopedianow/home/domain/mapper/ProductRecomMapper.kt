package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel

object ProductRecomMapper {
    fun mapProductRecomDataModel(response: HomeLayoutResponse, state: HomeLayoutItemState): HomeLayoutItemUiModel {
        val channelModel = ChannelMapper.mapToChannelModel(response)
        val productRecom = HomeProductRecomUiModel(channelModel.id, mapChannelToRecommendationWidget(channelModel))
        return HomeLayoutItemUiModel(productRecom, state)
    }

    private fun mapChannelToRecommendationWidget(channel: ChannelModel): RecommendationWidget {
        return RecommendationWidget(
            title = channel.channelHeader.name,
            subtitle = channel.channelHeader.subtitle,
            pageName = channel.pageName,
            seeMoreAppLink = channel.channelHeader.applink,
            recommendationItemList = mapChannelGridToRecommendationItem(channel.channelGrids)
        )
    }

    private fun mapChannelGridToRecommendationItem(channelGrids: List<ChannelGrid>): List<RecommendationItem> {
        val recommendationItems = mutableListOf<RecommendationItem>()
        channelGrids.forEach { grid ->
            recommendationItems.add(
                RecommendationItem(
                        productId = grid.id.toIntOrZero(),
                        name = grid.name,
                        price = grid.price,
                        rating = grid.rating,
                        ratingAverage = grid.ratingFloat,
                        slashedPrice = grid.slashedPrice,
                        imageUrl = grid.imageUrl,
                        minOrder = grid.minOrder,
                        stock = grid.stock,
                        discountPercentage = grid.discount,
                        shopId = grid.shopId.toIntOrZero(),
                        appUrl = grid.applink,
                        parentID = grid.parentProductId.toIntOrZero(),
                        isRecomProductShowVariantAndCart = true,
                        isTopAds = grid.isTopads,
                        isFreeOngkirActive = grid.isFreeOngkirActive,
                        freeOngkirImageUrl = grid.freeOngkirImageUrl,
                        recommendationType = grid.recommendationType,
                        isGold = grid.shop.isGoldMerchant,
                        isOfficial = grid.shop.isOfficialStore,
                        labelGroupList = grid.labelGroup.map {
                            RecommendationLabel(title = it.title, type = it.type, position = it.position, imageUrl = it.url)
                        }
                )
            )
        }
        return recommendationItems
    }
}

