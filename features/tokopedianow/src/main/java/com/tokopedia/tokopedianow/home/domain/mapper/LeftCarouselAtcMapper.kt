package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.util.ServerTimeOffsetUtil
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.domain.mapper.ChannelMapper.mapToChannelModel
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardSpaceUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardSeeMoreUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcUiModel

object LeftCarouselAtcMapper {

    private const val DEFAULT_PARENT_PRODUCT_ID = "0"

    fun mapToLeftCarouselAtc(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState,
        miniCartData: MiniCartSimplifiedData?
    ): HomeLayoutItemUiModel {
        val channelModel = mapToChannelModel(response)
        val productList = mutableListOf<Visitable<*>>()

        // Add front space to make distance
        productList.add(
            HomeLeftCarouselAtcProductCardSpaceUiModel(
                channelId = channelModel.id,
                channelHeaderName = channelModel.channelHeader.name,
                appLink = channelModel.channelBanner.applink
            )
        )

        // Add mix left carousel products
        channelModel.channelGrids.forEach {
            productList.add(
                HomeLeftCarouselAtcProductCardUiModel(
                    id = it.id,
                    brandId = it.brandId,
                    categoryId = it.categoryId,
                    parentProductId = it.parentProductId,
                    shopId = it.shopId,
                    shopName = it.shop.shopName,
                    appLink = it.applink,
                    channelId = channelModel.id,
                    channelHeaderName = channelModel.channelHeader.name,
                    channelPageName = channelModel.pageName,
                    channelType = channelModel.type,
                    recommendationType = it.recommendationType,
                    warehouseId = it.warehouseId,
                    campaignCode = it.campaignCode,
                    productCardModel = mapToProductCardModel(it, miniCartData)
                )
            )
        }

        // Add see more at the end of products
        if(channelModel.channelGrids.size > 1 && channelModel.channelHeader.applink.isNotEmpty()) {
            productList.add(
                HomeLeftCarouselAtcProductCardSeeMoreUiModel(
                    channelId = channelModel.id,
                    channelHeaderName = channelModel.channelHeader.name,
                    appLink = channelModel.channelHeader.applink,
                    backgroundImage = channelModel.channelHeader.backImage
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

    private fun mapToProductCardModel(channelGrid: ChannelGrid, miniCartData: MiniCartSimplifiedData? = null): ProductCardModel {
        val quantity = HomeLayoutMapper.getAddToCartQuantity(channelGrid.id, miniCartData)

        return if (isVariant(channelGrid.parentProductId)) {
            ProductCardModel(
                slashedPrice = channelGrid.slashedPrice,
                productName = channelGrid.name,
                formattedPrice = channelGrid.price,
                productImageUrl = channelGrid.imageUrl,
                discountPercentage = channelGrid.discount,
                pdpViewCount = channelGrid.productViewCountFormatted,
                stockBarLabel = channelGrid.label,
                isTopAds = channelGrid.isTopads,
                stockBarPercentage = channelGrid.soldPercentage,
                shopLocation = channelGrid.shop.shopLocation,
                shopBadgeList = channelGrid.badges.map {
                    ProductCardModel.ShopBadge(imageUrl = it.imageUrl)
                },
                labelGroupList = channelGrid.labelGroup.map {
                    ProductCardModel.LabelGroup(
                        position = it.position,
                        title = it.title,
                        type = it.type,
                        imageUrl = it.url
                    )
                },
                freeOngkir = ProductCardModel.FreeOngkir(
                    channelGrid.isFreeOngkirActive,
                    channelGrid.freeOngkirImageUrl
                ),
                isOutOfStock = channelGrid.isOutOfStock,
                ratingCount = channelGrid.rating,
                countSoldRating = channelGrid.ratingFloat,
                reviewCount = channelGrid.countReview,
                variant = ProductCardModel.Variant(quantity)
            )
        } else {
            ProductCardModel(
                slashedPrice = channelGrid.slashedPrice,
                productName = channelGrid.name,
                formattedPrice = channelGrid.price,
                productImageUrl = channelGrid.imageUrl,
                discountPercentage = channelGrid.discount,
                pdpViewCount = channelGrid.productViewCountFormatted,
                stockBarLabel = channelGrid.label,
                isTopAds = channelGrid.isTopads,
                stockBarPercentage = channelGrid.soldPercentage,
                shopLocation = channelGrid.shop.shopLocation,
                shopBadgeList = channelGrid.badges.map {
                    ProductCardModel.ShopBadge(imageUrl = it.imageUrl)
                },
                labelGroupList = channelGrid.labelGroup.map {
                    ProductCardModel.LabelGroup(
                        position = it.position,
                        title = it.title,
                        type = it.type,
                        imageUrl = it.url
                    )
                },
                freeOngkir = ProductCardModel.FreeOngkir(
                    channelGrid.isFreeOngkirActive,
                    channelGrid.freeOngkirImageUrl
                ),
                hasAddToCartButton = !channelGrid.isOutOfStock,
                isOutOfStock = channelGrid.isOutOfStock,
                ratingCount = channelGrid.rating,
                countSoldRating = channelGrid.ratingFloat,
                reviewCount = channelGrid.countReview,
                nonVariant = ProductCardModel.NonVariant(
                    quantity = quantity,
                    minQuantity = channelGrid.minOrder,
                    maxQuantity = channelGrid.stock
                )
            )
        }
    }

    private fun isVariant(parentProductId: String): Boolean {
        return parentProductId != DEFAULT_PARENT_PRODUCT_ID && parentProductId.isNotBlank()
    }
}