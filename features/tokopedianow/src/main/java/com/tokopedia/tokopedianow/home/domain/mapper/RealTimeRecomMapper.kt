package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.MIX_LEFT_CAROUSEL_ATC
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.PRODUCT_RECOM
import com.tokopedia.tokopedianow.home.domain.mapper.LeftCarouselAtcMapper.mapLeftAtcRealTimeRecomState
import com.tokopedia.tokopedianow.home.domain.mapper.LeftCarouselAtcMapper.mapLoadingLeftAtcRTR
import com.tokopedia.tokopedianow.home.domain.mapper.ProductRecomMapper.mapLoadingRealTimeRecomData
import com.tokopedia.tokopedianow.home.domain.mapper.ProductRecomMapper.mapRealTimeRecomWidgetState
import com.tokopedia.tokopedianow.home.domain.mapper.VisitableMapper.updateItemById
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeProductRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomProductUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeRealTimeRecomUiModel.RealTimeRecomWidgetState

object RealTimeRecomMapper {

    fun MutableList<HomeLayoutItemUiModel>.mapRealTimeRecommendation(
        channelId: String,
        recomWidget: RecommendationWidget,
        miniCartData: MiniCartSimplifiedData?,
        @TokoNowLayoutType type: String
    ) {
        when (type) {
            PRODUCT_RECOM -> mapProductRecomRTR(channelId, recomWidget, miniCartData)
            MIX_LEFT_CAROUSEL_ATC -> mapMixLeftAtcRTR(channelId, recomWidget, miniCartData)
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapLatestRealTimeRecommendation(
        channelId: String,
        @TokoNowLayoutType type: String
    ) {
        val realTimeRecom = getRealTimeRecom(channelId)
        val productId = realTimeRecom?.parentProductId.orEmpty()
        val rtrWidget = realTimeRecom?.widget

        if (rtrWidget != null) {
            mapRealTimeRecomState(channelId, productId, RealTimeRecomWidgetState.READY, type)
        } else {
            mapRealTimeRecomState(channelId, productId, RealTimeRecomWidgetState.IDLE, type)
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapRefreshRealTimeRecommendation(
        channelId: String,
        productId: String
    ) {
        val realTimeRecom = getRealTimeRecom(channelId)

        when (realTimeRecom?.type) {
            PRODUCT_RECOM -> mapRefreshProductRecomRTR(channelId, productId)
            MIX_LEFT_CAROUSEL_ATC -> mapRefreshLeftAtcRTR(channelId, productId)
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapRealTimeRecomState(
        channelId: String,
        productId: String,
        widgetState: RealTimeRecomWidgetState,
        @TokoNowLayoutType type: String
    ) {
        when (type) {
            PRODUCT_RECOM -> mapProductRealTimeRecomState(channelId, productId, widgetState)
            MIX_LEFT_CAROUSEL_ATC -> mapLeftAtcRealTimeRecomState(channelId, productId, widgetState)
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.mapLoadingRealTimeRecommendation(
        channelId: String,
        @TokoNowLayoutType type: String
    ) {
        when (type) {
            PRODUCT_RECOM -> mapLoadingProductRecomRTR(channelId)
            MIX_LEFT_CAROUSEL_ATC -> mapLoadingLeftAtcRTR(channelId)
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.removeRealTimeRecommendation(
        channelId: String,
        @TokoNowLayoutType type: String
    ) {
        when (type) {
            PRODUCT_RECOM -> removeProductRecomRTR(channelId)
            MIX_LEFT_CAROUSEL_ATC -> removeMixLeftAtcRTR(channelId)
        }
    }

    fun MutableList<HomeLayoutItemUiModel>.getRealTimeRecom(channelId: String): HomeRealTimeRecomUiModel? {
        return getProductRecomItem(channelId)?.realTimeRecom ?: getLeftAtcItem(channelId)?.realTimeRecom
    }

    private fun MutableList<HomeLayoutItemUiModel>.getProductRecomItem(channelId: String): HomeProductRecomUiModel? {
        return map { it.layout }.filterIsInstance<HomeProductRecomUiModel>().firstOrNull { it.id == channelId }
    }

    private fun MutableList<HomeLayoutItemUiModel>.getLeftAtcItem(channelId: String): HomeLeftCarouselAtcUiModel? {
        return map { it.layout }.filterIsInstance<HomeLeftCarouselAtcUiModel>().firstOrNull { it.id == channelId }
    }

    private fun HomeProductRecomUiModel.getRecomItem(productId: String?): HomeRealTimeRecomProductUiModel? {
        return getProductRecom(productId) ?: getProductRecomRtr(productId)
    }

    private fun HomeProductRecomUiModel.getProductRecomRtr(productId: String?): HomeRealTimeRecomProductUiModel? {
        return realTimeRecom.widget?.recommendationItemList?.firstOrNull { it.productId.toString() == productId }?.let {
            HomeRealTimeRecomProductUiModel(it.productId.toString(), it.imageUrl, it.categoryBreadcrumbs)
        }
    }

    private fun HomeProductRecomUiModel.getProductRecom(productId: String?): HomeRealTimeRecomProductUiModel? {
        return recomWidget.recommendationItemList.firstOrNull { it.productId.toString() == productId }?.let {
            HomeRealTimeRecomProductUiModel(it.productId.toString(), it.imageUrl, it.categoryBreadcrumbs)
        }
    }

    private fun HomeLeftCarouselAtcUiModel.getLeftAtcRecomItem(productId: String?): HomeRealTimeRecomProductUiModel? {
        return mapLeftAtcToRtrProduct(productId) ?: mapProductRecomToRtrProduct(productId)
    }

    private fun HomeLeftCarouselAtcUiModel.mapLeftAtcToRtrProduct(productId: String?): HomeRealTimeRecomProductUiModel? {
        val productList = productList.filterIsInstance<HomeLeftCarouselAtcProductCardUiModel>()
        return productList.firstOrNull { it.id.toString() == productId }?.let {
            val imageUrl = it.productCardModel.productImageUrl
            HomeRealTimeRecomProductUiModel(it.id.toString(), imageUrl, it.categoryBreadcrumbs)
        }
    }

    private fun HomeLeftCarouselAtcUiModel.mapProductRecomToRtrProduct(productId: String?): HomeRealTimeRecomProductUiModel? {
        val recommendationItemList = realTimeRecom.widget?.recommendationItemList
        return recommendationItemList?.firstOrNull { it.productId.toString() == productId }?.let {
            HomeRealTimeRecomProductUiModel(it.productId.toString(), it.imageUrl, it.categoryBreadcrumbs)
        }
    }

    private fun MutableList<HomeLayoutItemUiModel>.mapRefreshLeftAtcRTR(
        channelId: String,
        productId: String
    ) {
        getLeftAtcItem(channelId)?.let { item ->
            updateItemById(item.id) {
                mapLeftAtcRealTimeRecomState(productId, RealTimeRecomWidgetState.REFRESH, item)
            }
        }
    }

    private fun MutableList<HomeLayoutItemUiModel>.mapRefreshProductRecomRTR(
        channelId: String,
        productId: String
    ) {
        getProductRecomItem(channelId)?.let { item ->
            updateItemById(item.id) {
                mapRealTimeRecomWidgetState(productId, RealTimeRecomWidgetState.REFRESH, item)
            }
        }
    }

    private fun MutableList<HomeLayoutItemUiModel>.mapLeftAtcRealTimeRecomState(
        channelId: String,
        productId: String,
        state: RealTimeRecomWidgetState
    ) {
        getLeftAtcItem(channelId)?.let { item ->
            updateItemById(item.id) {
                mapLeftAtcRealTimeRecomState(productId, state, item)
            }
        }
    }

    private fun MutableList<HomeLayoutItemUiModel>.mapProductRealTimeRecomState(
        channelId: String,
        productId: String,
        state: RealTimeRecomWidgetState
    ) {
        getProductRecomItem(channelId)?.let { item ->
            updateItemById(item.id) {
                mapRealTimeRecomWidgetState(productId, state, item)
            }
        }
    }

    private fun MutableList<HomeLayoutItemUiModel>.mapLoadingLeftAtcRTR(channelId: String) {
        getLeftAtcItem(channelId)?.let { item ->
            updateItemById(item.id) {
                mapLoadingLeftAtcRTR(item)
            }
        }
    }

    private fun MutableList<HomeLayoutItemUiModel>.mapLoadingProductRecomRTR(channelId: String) {
        getProductRecomItem(channelId)?.let { item ->
            updateItemById(item.id) {
                mapLoadingRealTimeRecomData(item)
            }
        }
    }

    private fun MutableList<HomeLayoutItemUiModel>.mapProductRecomRTR(
        channelId: String,
        recomWidget: RecommendationWidget,
        miniCartData: MiniCartSimplifiedData?
    ) {
        val item = getProductRecomItem(channelId)
        val realTimeRecom = item?.realTimeRecom
        val productId = realTimeRecom?.parentProductId

        item?.getRecomItem(productId)?.let {
            updateItemById(channelId) {
                ProductRecomMapper.mapRealTimeRecomData(item, recomWidget, it, miniCartData)
            }
        }
    }

    private fun MutableList<HomeLayoutItemUiModel>.mapMixLeftAtcRTR(
        channelId: String,
        recomWidget: RecommendationWidget,
        miniCartData: MiniCartSimplifiedData?
    ) {
        val item = getLeftAtcItem(channelId)
        val realTimeRecom = item?.realTimeRecom
        val productId = realTimeRecom?.parentProductId

        item?.getLeftAtcRecomItem(productId)?.let {
            updateItemById(channelId) {
                LeftCarouselAtcMapper.mapLeftAtcRTR(item, recomWidget, it, miniCartData)
            }
        }
    }

    private fun MutableList<HomeLayoutItemUiModel>.removeMixLeftAtcRTR(channelId: String) {
        getLeftAtcItem(channelId)?.let {
            updateItemById(it.id) {
                LeftCarouselAtcMapper.removeLeftAtcRTR(it)
            }
        }
    }

    private fun MutableList<HomeLayoutItemUiModel>.removeProductRecomRTR(channelId: String) {
        getProductRecomItem(channelId)?.let {
            updateItemById(it.id) {
                ProductRecomMapper.removeRealTimeRecomData(it)
            }
        }
    }
}
