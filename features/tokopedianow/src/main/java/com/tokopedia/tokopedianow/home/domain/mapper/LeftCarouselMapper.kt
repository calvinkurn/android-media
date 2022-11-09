package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.common.model.LabelGroup
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.mapper.ChannelMapper.mapToChannelModel
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardSeeMoreUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcUiModel

object LeftCarouselMapper {
    private const val DEFAULT_PARENT_PRODUCT_ID = "0"
    private const val DEFAULT_MAX_ORDER = 0

    fun mapResponseToLeftCarousel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState,
        miniCartData: MiniCartSimplifiedData? = null
    ): HomeLayoutItemUiModel {
        val channelModel = mapToChannelModel(response)
        val productList = mutableListOf<Visitable<*>>()

        // Add mix left carousel products
        channelModel.channelGrids.forEach { channelGrid ->
            productList.add(
                HomeLeftCarouselAtcProductCardUiModel(
                    id = channelGrid.id,
                    brandId = channelGrid.brandId,
                    categoryId = channelGrid.categoryId,
                    parentProductId = channelGrid.parentProductId,
                    shopId = channelGrid.shopId,
                    shopName = channelGrid.shop.shopName,
                    appLink = channelGrid.applink,
                    channelId = channelModel.id,
                    channelHeaderName = channelModel.channelHeader.name,
                    channelPageName = channelModel.pageName,
                    channelType = channelModel.type,
                    recommendationType = channelGrid.recommendationType,
                    warehouseId = channelGrid.warehouseId,
                    campaignCode = channelGrid.campaignCode,
                    productCardModel = mapChannelGridToProductCard(channelGrid, miniCartData),
                )
            )
        }

        // Add see more at the end of the list
        if(channelModel.channelHeader.applink.isNotEmpty()) {
            productList.add(
                TokoNowSeeMoreCardCarouselUiModel(
                    channelId = channelModel.id,
                    channelHeaderName = channelModel.channelHeader.name,
                    appLink = channelModel.channelHeader.applink
                )
            )
        }

        val layout = HomeLeftCarouselAtcUiModel(
            id = channelModel.id,
            name = channelModel.name,
            header = TokoNowDynamicHeaderUiModel(
                title = channelModel.channelHeader.name,
                subTitle = channelModel.channelHeader.subtitle,
                ctaText = "",
                ctaTextLink = channelModel.channelHeader.applink,
                expiredTime = channelModel.channelHeader.expiredTime,
                serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(channelModel.channelHeader.serverTimeUnix),
                backColor = channelModel.channelHeader.backColor
            ),
            productList = productList,
            backgroundColorArray = channelModel.channelBanner.gradientColor,
            imageBanner = channelModel.channelBanner.imageUrl,
            imageBannerAppLink = channelModel.channelBanner.applink,
        )

        return HomeLayoutItemUiModel(
            layout = layout,
            state = state
        )
    }

    private fun mapChannelGridToProductCard(
        channelGrid: ChannelGrid,
        miniCartData: MiniCartSimplifiedData? = null
    ): TokoNowProductCardViewUiModel = TokoNowProductCardViewUiModel(
        productId = channelGrid.id,
        imageUrl = channelGrid.imageUrl,
        minOrder = channelGrid.minOrder,
        maxOrder = channelGrid.maxOrder,
        availableStock = channelGrid.stock,
        orderQuantity = HomeLayoutMapper.getAddToCartQuantity(channelGrid.id, miniCartData),
        price = channelGrid.price,
        discount = channelGrid.discount,
        slashPrice = channelGrid.slashedPrice,
        name = channelGrid.name,
        rating = channelGrid.ratingFloat,
        progressBarLabel = channelGrid.label,
        progressBarPercentage = channelGrid.soldPercentage,
        isVariant = channelGrid.parentProductId != DEFAULT_PARENT_PRODUCT_ID && channelGrid.parentProductId.isNotBlank(),
        needToShowQuantityEditor = channelGrid.minOrder <= channelGrid.maxOrder && channelGrid.maxOrder != DEFAULT_MAX_ORDER,
        labelGroupList = channelGrid.labelGroup.map {
            LabelGroup(
                position = it.position,
                type = it.type,
                title = it.title,
                imageUrl = it.url
            )
        }
    )
}
