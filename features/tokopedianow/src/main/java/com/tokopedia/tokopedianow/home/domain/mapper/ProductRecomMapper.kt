package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelShop
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel.LabelGroup
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.getAddToCartQuantity
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel

object ProductRecomMapper {
    private const val DEFAULT_PARENT_PRODUCT_ID = "0"
    private const val DEFAULT_MAX_ORDER = 0
    private const val SHOP_TYPE_GOLD = "gold"
    private const val SHOP_TYPE_OS = "os"
    private const val SHOP_TYPE_PM = "pm"

    private fun mapChannelGridToProductCard(
        channelGrid: ChannelGrid,
        miniCartData: MiniCartSimplifiedData? = null
    ): TokoNowProductCardViewUiModel = TokoNowProductCardViewUiModel(
        productId = channelGrid.id,
        imageUrl = channelGrid.imageUrl,
        minOrder = channelGrid.minOrder,
        maxOrder = channelGrid.maxOrder,
        availableStock = channelGrid.stock,
        orderQuantity = getAddToCartQuantity(channelGrid.id, miniCartData),
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
        },
        usePreDraw = true
    )

    private fun getShopType(shop: ChannelShop): String{
        return if (shop.isGoldMerchant) SHOP_TYPE_GOLD
        else if (shop.isOfficialStore) SHOP_TYPE_OS
        else SHOP_TYPE_PM
    }

    fun mapResponseToProductRecom(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState,
        miniCartData: MiniCartSimplifiedData? = null
    ): HomeLayoutItemUiModel {
        val channelModel = ChannelMapper.mapToChannelModel(response)

        val layout = HomeProductRecomUiModel(
            id = channelModel.id,
            title = channelModel.name,
            productList = channelModel.channelGrids.map { channelGrid ->
                TokoNowProductCardCarouselItemUiModel(
                    recomType = channelGrid.recommendationType,
                    pageName = channelModel.pageName,
                    productCardModel = mapChannelGridToProductCard(channelGrid, miniCartData),
                    shopId = channelGrid.shopId,
                    shopName = channelGrid.shop.shopName,
                    shopType = getShopType(channelGrid.shop),
                    isTopAds = channelGrid.isTopads,
                    appLink = channelGrid.applink,
                    parentId = channelGrid.parentProductId
                )
            },
            seeMoreModel = TokoNowSeeMoreCardCarouselUiModel(
                id = channelModel.channelHeader.id,
                headerName = channelModel.channelHeader.name,
                appLink = channelModel.channelHeader.applink
            ),
            headerModel = TokoNowDynamicHeaderUiModel(
                title = channelModel.channelHeader.name,
                subTitle = channelModel.channelHeader.subtitle,
                ctaText = "",
                ctaTextLink = channelModel.channelHeader.applink,
                expiredTime = channelModel.channelHeader.expiredTime,
                serverTimeOffset = ServerTimeOffsetUtil.getServerTimeOffsetFromUnix(channelModel.channelHeader.serverTimeUnix),
                backColor = channelModel.channelHeader.backColor
            )
        )

        return HomeLayoutItemUiModel(
            layout = layout,
            state = state,
        )
    }
}

