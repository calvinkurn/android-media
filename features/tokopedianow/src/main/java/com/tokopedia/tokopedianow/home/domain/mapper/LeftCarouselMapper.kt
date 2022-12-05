package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.MIX_LEFT_CAROUSEL_ATC
import com.tokopedia.tokopedianow.common.constant.TokoNowProductRecommendationState
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel.LabelGroup
import com.tokopedia.tokopedianow.common.model.TokoNowDynamicHeaderUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.util.QueryParamUtil.getBooleanValue
import com.tokopedia.tokopedianow.common.util.QueryParamUtil.getStringValue
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.constant.HomeRealTimeRecomParam.PARAM_RTR_INTERACTION
import com.tokopedia.tokopedianow.home.constant.HomeRealTimeRecomParam.PARAM_RTR_PAGENAME
import com.tokopedia.tokopedianow.home.domain.mapper.ChannelMapper.mapToChannelModel
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.getAddToCartQuantity
import com.tokopedia.tokopedianow.home.domain.mapper.ProductCardMapper.mapRecomWidgetToProductList
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomProductUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel

object LeftCarouselMapper {
    private const val DEFAULT_PARENT_PRODUCT_ID = "0"
    private const val CATEGORY_DIVIDER = "/"

    fun mapResponseToLeftCarousel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState,
        miniCartData: MiniCartSimplifiedData? = null,
        warehouseId: String,
        layoutType: String
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
                    productCardModel = mapChannelGridToProductCard(channelGrid, miniCartData, layoutType),
                    categoryBreadcrumbs = channelGrid.categoryBreadcrumbs
                )
            )
        }

        // Add see more at the end of the list
        if(channelModel.channelHeader.applink.isNotEmpty()) {
            productList.add(
                TokoNowSeeMoreCardCarouselUiModel(
                    id = channelModel.id,
                    headerName = channelModel.channelHeader.name,
                    appLink = channelModel.channelHeader.applink
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
                type = layoutType
            )
        )

        return HomeLayoutItemUiModel(
            layout = layout,
            state = state
        )
    }

    private fun mapChannelGridToProductCard(
        channelGrid: ChannelGrid,
        miniCartData: MiniCartSimplifiedData? = null,
        layoutType: String
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
        needToShowQuantityEditor = layoutType == MIX_LEFT_CAROUSEL_ATC,
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

    fun mapLeftAtcRTR(
        item: HomeLeftCarouselAtcUiModel,
        recomWidget: RecommendationWidget,
        parentProduct: HomeRealTimeRecomProductUiModel,
        miniCartData: MiniCartSimplifiedData?
    ): HomeLayoutItemUiModel {
        val headerName = item.header.title
        val productList = mapRecomWidgetToProductList(headerName, recomWidget, miniCartData, true)
        val categoryBreadcrumbs = parentProduct.categoryBreadcrumbs

        val realTimeRecom = item.realTimeRecom.copy(
            parentProductId = parentProduct.id,
            productImageUrl = parentProduct.imageUrl,
            category = categoryBreadcrumbs.substringAfterLast(CATEGORY_DIVIDER),
            productList = productList,
            widgetState = HomeRealTimeRecomUiModel.RealTimeRecomWidgetState.READY,
            carouselState = TokoNowProductRecommendationState.LOADED,
            type = MIX_LEFT_CAROUSEL_ATC
        )

        val homeRtrWidgetItem = item.copy(realTimeRecom = realTimeRecom)
        return HomeLayoutItemUiModel(homeRtrWidgetItem, HomeLayoutItemState.LOADED)
    }

    fun mapLeftAtcRealTimeRecomState(
        productId: String,
        state: HomeRealTimeRecomUiModel.RealTimeRecomWidgetState,
        item: HomeLeftCarouselAtcUiModel
    ): HomeLayoutItemUiModel {
        val realTimeRecom = item.realTimeRecom.copy(
            parentProductId = productId,
            widgetState = state,
            carouselState = TokoNowProductRecommendationState.LOADED
        )
        val homeRtrWidgetItem = item.copy(realTimeRecom = realTimeRecom)
        return HomeLayoutItemUiModel(homeRtrWidgetItem, HomeLayoutItemState.LOADED)
    }

    fun mapLoadingLeftAtcRTR(item: HomeLeftCarouselAtcUiModel): HomeLayoutItemUiModel {
        val realTimeRecom = item.realTimeRecom.copy(
            widgetState = HomeRealTimeRecomUiModel.RealTimeRecomWidgetState.READY,
            carouselState = TokoNowProductRecommendationState.LOADING
        )
        val homeRtrWidgetItem = item.copy(realTimeRecom = realTimeRecom)
        return HomeLayoutItemUiModel(homeRtrWidgetItem, HomeLayoutItemState.LOADED)
    }

    fun removeLeftAtcRTR(item: HomeLeftCarouselAtcUiModel): HomeLayoutItemUiModel {
        val recomWidget = item.copy(realTimeRecom = item.realTimeRecom.copy(productList = emptyList()))
        return HomeLayoutItemUiModel(recomWidget, HomeLayoutItemState.LOADED)
    }
}
