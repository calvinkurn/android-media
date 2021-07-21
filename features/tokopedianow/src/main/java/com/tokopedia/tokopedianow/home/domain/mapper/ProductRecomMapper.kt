package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
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
                        isRecomProductShowVariantAndCart = true
                )
            )
        }
        recommendationItems.add(
            RecommendationItem(
                productId = 1920777141,
                name = "PDP D4G1NG 1G4 N0NV4R SL4SH PR1C3",
                price = "Rp 500",
                rating = 0,
                ratingAverage = "0",
                slashedPrice = "Rp 1.000",
                imageUrl = "https://images.tokopedia.net/img/cache/300-square/hDjmkQ/2021/6/14/0f76bf9a-bfa2-4b97-94f8-3226d671c2a8.jpg",
                minOrder = 1,
                stock = 500,
                discountPercentage = "50%",
                shopId = 11515028,
                appUrl = "tokopedia://product/1920777141",
                parentID = 0,
                isRecomProductShowVariantAndCart = true
            )
        )
        return recommendationItems
    }
}

