package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.home_component.model.ChannelGrid
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationLabel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.common.constant.ConstantValue.ADDITIONAL_POSITION
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.util.QueryParamUtil.getBooleanValue
import com.tokopedia.tokopedianow.common.util.QueryParamUtil.getStringValue
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.constant.HomeRealTimeRecomParam.PARAM_RTR_INTERACTION
import com.tokopedia.tokopedianow.home.constant.HomeRealTimeRecomParam.PARAM_RTR_PAGENAME
import com.tokopedia.tokopedianow.home.domain.mapper.HomeLayoutMapper.getAddToCartQuantity
import com.tokopedia.tokopedianow.home.domain.model.HomeLayoutResponse
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomProductUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel.RealTimeRecomWidgetState

object ProductRecomMapper {

    private const val CATEGORY_DIVIDER = "/"
    private const val DEFAULT_TITLE = ""

    fun mapProductRecomDataModel(
        response: HomeLayoutResponse,
        state: HomeLayoutItemState,
        miniCartData: MiniCartSimplifiedData? = null,
        warehouseId: String
    ): HomeLayoutItemUiModel {
        val channelModel = ChannelMapper.mapToChannelModel(response)
        val recomWidget = mapChannelToRecommendationWidget(channelModel, miniCartData)

        val widgetParam = channelModel.widgetParam
        val pageName = widgetParam.getStringValue(PARAM_RTR_PAGENAME)
        val enableRTR = widgetParam.getBooleanValue(PARAM_RTR_INTERACTION)

        val productRecom = HomeProductRecomUiModel(
            id = channelModel.id,
            recomWidget = recomWidget,
            realTimeRecom = HomeRealTimeRecomUiModel(
                channelId = channelModel.id,
                headerName = channelModel.channelHeader.name,
                warehouseId = warehouseId,
                pageName = pageName,
                enabled = enableRTR,
                type = TokoNowLayoutType.PRODUCT_RECOM
            )
        )

        return HomeLayoutItemUiModel(productRecom, state)
    }

    fun mapRealTimeRecomData(
        item: HomeProductRecomUiModel,
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
            carouselState = RecommendationCarouselData.STATE_READY
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
            carouselState = RecommendationCarouselData.STATE_READY
        )
        val homeRtrWidgetItem = item.copy(realTimeRecom = realTimeRecom)
        return HomeLayoutItemUiModel(homeRtrWidgetItem, HomeLayoutItemState.LOADED)
    }

    fun mapLoadingRealTimeRecomData(item: HomeProductRecomUiModel): HomeLayoutItemUiModel {
        val realTimeRecom = item.realTimeRecom.copy(
            widgetState = RealTimeRecomWidgetState.READY,
            carouselState = RecommendationCarouselData.STATE_LOADING
        )
        val homeRtrWidgetItem = item.copy(realTimeRecom = realTimeRecom)
        return HomeLayoutItemUiModel(homeRtrWidgetItem, HomeLayoutItemState.LOADED)
    }

    fun removeRealTimeRecomData(item: HomeProductRecomUiModel): HomeLayoutItemUiModel {
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

    private fun mapChannelToRecommendationWidget(
        channel: ChannelModel,
        miniCartData: MiniCartSimplifiedData? = null
    ): RecommendationWidget {
        return RecommendationWidget(
            title = channel.channelHeader.name,
            subtitle = channel.channelHeader.subtitle,
            pageName = channel.pageName,
            seeMoreAppLink = channel.channelHeader.applink,
            recommendationItemList = mapChannelGridToRecommendationItem(
                channel.channelGrids,
                channel.pageName,
                miniCartData
            )
        )
    }

    private fun mapChannelGridToRecommendationItem(
        channelGrids: List<ChannelGrid>,
        pageName: String,
        miniCartData: MiniCartSimplifiedData? = null
    ): List<RecommendationItem> {
        val recommendationItems = mutableListOf<RecommendationItem>()
        channelGrids.forEachIndexed { index, grid ->
            val quantity = getAddToCartQuantity(grid.id, miniCartData)

            recommendationItems.add(
                RecommendationItem(
                        productId = grid.id.toLongOrZero(),
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
                        shopName = grid.shop.shopName,
                        appUrl = grid.applink,
                        pageName = pageName,
                        parentID = grid.parentProductId.toLongOrZero(),
                        isRecomProductShowVariantAndCart = true,
                        isTopAds = grid.isTopads,
                        isFreeOngkirActive = grid.isFreeOngkirActive,
                        freeOngkirImageUrl = grid.freeOngkirImageUrl,
                        recommendationType = grid.recommendationType,
                        isGold = grid.shop.isGoldMerchant,
                        isOfficial = grid.shop.isOfficialStore,
                        labelGroupList = grid.labelGroup.map {
                            RecommendationLabel(title = it.title, type = it.type, position = it.position, imageUrl = it.url)
                        },
                        quantity = quantity,
                        position = index + ADDITIONAL_POSITION,
                        categoryBreadcrumbs = grid.categoryBreadcrumbs
                )
            )
        }
        return recommendationItems
    }
}

