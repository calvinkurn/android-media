package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelShop
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowDynamicHeaderUiModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowProductRecommendationState
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel
import com.tokopedia.productcard_compact.productcard.presentation.uimodel.TokoNowProductCardViewUiModel.LabelGroup
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.util.QueryParamUtil.getBooleanValue
import com.tokopedia.tokopedianow.common.util.QueryParamUtil.getStringValue
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.constant.HomeRealTimeRecomParam.PARAM_RTR_INTERACTION
import com.tokopedia.tokopedianow.home.constant.HomeRealTimeRecomParam.PARAM_RTR_PAGENAME
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.getAddToCartQuantity
import com.tokopedia.tokopedianow.home.domain.mapper.ProductCardMapper.mapRecomWidgetToProductList
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomProductUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel.RealTimeRecomWidgetState

object ProductRecomMapper {
    private const val DEFAULT_PARENT_PRODUCT_ID = "0"
    private const val CATEGORY_DIVIDER = "/"

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
        needToShowQuantityEditor = true,
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
        miniCartData: MiniCartSimplifiedData? = null,
        warehouseId: String
    ): HomeLayoutItemUiModel {
        val channelModel = ChannelMapper.mapToChannelModel(response)

        val widgetParam = channelModel.widgetParam
        val pageName = widgetParam.getStringValue(PARAM_RTR_PAGENAME)
        val enableRTR = widgetParam.getBooleanValue(PARAM_RTR_INTERACTION)

        val layout = HomeProductRecomUiModel(
            id = channelModel.id,
            title = channelModel.channelHeader.name,
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
                    parentId = channelGrid.parentProductId,
                    categoryBreadcrumbs = channelGrid.categoryBreadcrumbs
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
                serverTimeOffset = channelModel.channelConfig.serverTimeOffset,
                backColor = channelModel.channelHeader.backColor
            ),
            realTimeRecom = HomeRealTimeRecomUiModel(
                channelId = channelModel.id,
                headerName = channelModel.channelHeader.name,
                warehouseId = warehouseId,
                pageName = pageName,
                enabled = enableRTR,
                type = TokoNowLayoutType.PRODUCT_RECOM
            )
        )

        return HomeLayoutItemUiModel(
            layout = layout,
            state = state,
        )
    }

    fun mapRealTimeRecomData(
        item: HomeProductRecomUiModel,
        recomWidget: RecommendationWidget,
        parentProduct: HomeRealTimeRecomProductUiModel,
        miniCartData: MiniCartSimplifiedData?
    ): HomeLayoutItemUiModel {
        val headerName = item.title
        val productList = mapRecomWidgetToProductList(headerName, recomWidget, miniCartData, true)
        val categoryBreadcrumbs = parentProduct.categoryBreadcrumbs

        val realTimeRecom = item.realTimeRecom.copy(
            parentProductId = parentProduct.id,
            productImageUrl = parentProduct.imageUrl,
            category = categoryBreadcrumbs.substringAfterLast(CATEGORY_DIVIDER),
            productList = productList,
            widgetState = RealTimeRecomWidgetState.READY,
            carouselState = TokoNowProductRecommendationState.LOADED
        )

        val homeRtrWidgetItem = item.copy(realTimeRecom = realTimeRecom)
        return HomeLayoutItemUiModel(homeRtrWidgetItem, HomeLayoutItemState.LOADED)
    }

    fun mapRealTimeRecomWidgetState(
        productId: String,
        state: RealTimeRecomWidgetState,
        item: HomeProductRecomUiModel
    ): HomeLayoutItemUiModel {
        val realTimeRecom = item.realTimeRecom.copy(
            parentProductId = productId,
            widgetState = state,
            carouselState = TokoNowProductRecommendationState.LOADED
        )
        val homeRtrWidgetItem = item.copy(realTimeRecom = realTimeRecom)
        return HomeLayoutItemUiModel(homeRtrWidgetItem, HomeLayoutItemState.LOADED)
    }

    fun mapLoadingRealTimeRecomData(item: HomeProductRecomUiModel): HomeLayoutItemUiModel {
        val realTimeRecom = item.realTimeRecom.copy(
            widgetState = RealTimeRecomWidgetState.READY,
            carouselState = TokoNowProductRecommendationState.LOADING
        )
        val homeRtrWidgetItem = item.copy(realTimeRecom = realTimeRecom)
        return HomeLayoutItemUiModel(homeRtrWidgetItem, HomeLayoutItemState.LOADED)
    }

    fun removeRealTimeRecomData(item: HomeProductRecomUiModel): HomeLayoutItemUiModel {
        val recomWidget = item.copy(realTimeRecom = item.realTimeRecom.copy(productList = emptyList()))
        return HomeLayoutItemUiModel(recomWidget, HomeLayoutItemState.LOADED)
    }
}

