package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.common.constant.ConstantValue.ADDITIONAL_POSITION
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.util.QueryParamUtil.getBooleanValue
import com.tokopedia.tokopedianow.common.util.QueryParamUtil.getStringValue
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.constant.HomeRealTimeRecomParam.PARAM_RTR_INTERACTION
import com.tokopedia.tokopedianow.home.constant.HomeRealTimeRecomParam.PARAM_RTR_PAGENAME
import com.tokopedia.tokopedianow.home.domain.mapper.ChannelMapper.mapToChannelModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.getAddToCartQuantity
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardSpaceUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardSeeMoreUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomProductUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel.RealTimeRecomWidgetState

/**
 * RTR: Real Time Recommendation
 * https://docs.google.com/document/d/1p6D0cPs2gZx50cEe50P9MVSTKPXhhS385uauk1Kmoys/edit?usp=sharing
 */
object LeftCarouselAtcMapper {

    private const val DEFAULT_PARENT_PRODUCT_ID = "0"
    private const val CATEGORY_DIVIDER = "/"
    private const val DEFAULT_TITLE = ""

    fun mapToLeftCarouselAtc(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState,
        miniCartData: MiniCartSimplifiedData?,
        warehouseId: String
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
        channelModel.channelGrids.forEachIndexed { index, channelGrid ->
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
                    categoryBreadcrumbs = channelGrid.categoryBreadcrumbs,
                    productCardModel = mapToProductCardModel(channelGrid, miniCartData),
                    position = index + ADDITIONAL_POSITION
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

        val widgetParam = channelModel.widgetParam
        val pageName = widgetParam.getStringValue(PARAM_RTR_PAGENAME)
        val enableRTR = widgetParam.getBooleanValue(PARAM_RTR_INTERACTION)

        val layout = HomeLeftCarouselAtcUiModel(
            id = channelModel.id,
            name = channelModel.name,
            header = TokoNowDynamicHeaderUiModel(
                title = channelModel.channelHeader.name,
                subTitle = channelModel.channelHeader.subtitle,
                ctaText = "",
                ctaTextLink = channelModel.channelHeader.applink,
                expiredTime = channelModel.channelHeader.expiredTime,
                serverTimeOffset = channelModel.channelConfig.serverTimeOffset,
                backColor = channelModel.channelHeader.backColor
            ),
            productList = productList,
            backgroundColorArray = channelModel.channelBanner.gradientColor,
            imageBanner = channelModel.channelBanner.imageUrl,
            imageBannerAppLink = channelModel.channelBanner.applink,
            realTimeRecom = HomeRealTimeRecomUiModel(
                channelId = channelModel.id,
                headerName = channelModel.channelHeader.name,
                warehouseId = warehouseId,
                pageName = pageName,
                enabled = enableRTR,
                type = TokoNowLayoutType.MIX_LEFT_CAROUSEL_ATC
            )
        )

        return HomeLayoutItemUiModel(
            layout = layout,
            state = state
        )
    }

    private fun mapToProductCardModel(channelGrid: ChannelGrid, miniCartData: MiniCartSimplifiedData? = null): ProductCardModel {
        val quantity = getAddToCartQuantity(channelGrid.id, miniCartData)

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
                isOutOfStock = channelGrid.isOutOfStock,
                ratingCount = channelGrid.rating,
                countSoldRating = channelGrid.ratingFloat,
                reviewCount = channelGrid.countReview,
                variant = if (!channelGrid.isOutOfStock) ProductCardModel.Variant(
                    quantity = quantity
                ) else null
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
                isOutOfStock = channelGrid.isOutOfStock,
                ratingCount = channelGrid.rating,
                countSoldRating = channelGrid.ratingFloat,
                reviewCount = channelGrid.countReview,
                nonVariant = if (!channelGrid.isOutOfStock) ProductCardModel.NonVariant(
                    quantity = quantity,
                    minQuantity = channelGrid.minOrder,
                    maxQuantity = channelGrid.stock
                ) else null
            )
        }
    }

    fun mapLeftAtcRTR(
        item: HomeLeftCarouselAtcUiModel,
        recomWidget: RecommendationWidget,
        parentProduct: HomeRealTimeRecomProductUiModel,
        miniCartData: MiniCartSimplifiedData?
    ): HomeLayoutItemUiModel {
        val recommendationItemList = mapCartQuantityToRecomItem(recomWidget, miniCartData)
        val realTimeRecomWidget = recomWidget.copy(
            title = DEFAULT_TITLE,
            recommendationItemList = recommendationItemList
        )
        val categoryBreadcrumbs = parentProduct.categoryBreadcrumbs

        val realTimeRecom = item.realTimeRecom.copy(
            parentProductId = parentProduct.id,
            productImageUrl = parentProduct.imageUrl,
            category = categoryBreadcrumbs.substringAfterLast(CATEGORY_DIVIDER),
            widget = realTimeRecomWidget,
            widgetState = RealTimeRecomWidgetState.READY,
            carouselState = RecommendationCarouselData.STATE_READY,
            type = TokoNowLayoutType.MIX_LEFT_CAROUSEL_ATC
        )

        val homeRtrWidgetItem = item.copy(realTimeRecom = realTimeRecom)
        return HomeLayoutItemUiModel(homeRtrWidgetItem, HomeLayoutItemState.LOADED)
    }

    fun mapLeftAtcRealTimeRecomState(
        productId: String,
        state: RealTimeRecomWidgetState,
        item: HomeLeftCarouselAtcUiModel
    ): HomeLayoutItemUiModel {
        val realTimeRecom = item.realTimeRecom.copy(
            parentProductId = productId,
            widgetState = state,
            carouselState = RecommendationCarouselData.STATE_READY
        )
        val homeRtrWidgetItem = item.copy(realTimeRecom = realTimeRecom)
        return HomeLayoutItemUiModel(homeRtrWidgetItem, HomeLayoutItemState.LOADED)
    }

    fun mapLoadingLeftAtcRTR(item: HomeLeftCarouselAtcUiModel): HomeLayoutItemUiModel {
        val realTimeRecom = item.realTimeRecom.copy(
            widgetState = RealTimeRecomWidgetState.READY,
            carouselState = RecommendationCarouselData.STATE_LOADING
        )
        val homeRtrWidgetItem = item.copy(realTimeRecom = realTimeRecom)
        return HomeLayoutItemUiModel(homeRtrWidgetItem, HomeLayoutItemState.LOADED)
    }

    fun removeLeftAtcRTR(item: HomeLeftCarouselAtcUiModel): HomeLayoutItemUiModel {
        val recomWidget = item.copy(realTimeRecom = item.realTimeRecom.copy(widget = null))
        return HomeLayoutItemUiModel(recomWidget, HomeLayoutItemState.LOADED)
    }

    private fun mapCartQuantityToRecomItem(
        recomWidget: RecommendationWidget,
        miniCartData: MiniCartSimplifiedData?
    ): List<RecommendationItem> {
        return recomWidget.recommendationItemList.map {
            val quantity = getAddToCartQuantity(it.productId.toString(), miniCartData)
            it.copy(quantity = quantity)
        }
    }

    private fun isVariant(parentProductId: String): Boolean {
        return parentProductId != DEFAULT_PARENT_PRODUCT_ID && parentProductId.isNotBlank()
    }
}
