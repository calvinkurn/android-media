package com.tokopedia.recommendation_widget_common.widget.global

import com.tokopedia.minicart.common.domain.data.MiniCartItem
import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData
import com.tokopedia.minicart.common.domain.data.getMiniCartItemProduct
import com.tokopedia.minicart.common.domain.usecase.MiniCartSource
import com.tokopedia.recommendation_widget_common.RecommendationTypeConst.TYPE_COMPARISON_BPC_WIDGET
import com.tokopedia.recommendation_widget_common.RecommendationTypeConst.TYPE_LIMIT_VERTICAL
import com.tokopedia.recommendation_widget_common.mvvm.UiState
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.global.RecommendationCarouselModel
import com.tokopedia.recommendation_widget_common.widget.comparison_bpc.RecommendationComparisonBpcModel
import com.tokopedia.recommendation_widget_common.widget.loading.RecommendationCarouselShimmeringModel
import com.tokopedia.recommendation_widget_common.widget.vertical.RecommendationVerticalModel

data class RecommendationWidgetState(
    val widgetMap: Map<String, List<RecommendationVisitable>> = mapOf(),
    val miniCartShopId: String = "",
    val miniCartSource: MiniCartSource? = null,
    val miniCartData: MiniCartSimplifiedData? = null,
    val successMessage: String? = null,
    val errorMessage: String? = null,
): UiState {

    fun from(
        model: RecommendationWidgetModel,
        widget: List<RecommendationWidget>,
    ): RecommendationWidgetState = copy(
        widgetMap = widgetMap + mapOf(
            model.id to recommendationVisitableList(widget, model)
        ),
        miniCartShopId = firstShopId(widget),
        miniCartSource = model.miniCart.miniCartSource,
    )

    private fun firstShopId(widgetList: List<RecommendationWidget>) =
        widgetList.map(::recommendationItemQuantityEditor)
            .flatten()
            .firstOrNull()
            ?.toString() ?: ""

    private fun recommendationItemQuantityEditor(widget: RecommendationWidget): List<Int> =
        widget.recommendationItemList
            .filter(RecommendationItem::isUseQuantityEditor)
            .map(RecommendationItem::shopId)

    private fun recommendationVisitableList(
        widget: List<RecommendationWidget>,
        model: RecommendationWidgetModel,
    ): List<RecommendationVisitable> {
        return if (widget.all { it.recommendationItemList.isEmpty() }) emptyList()
        else widget.map { recommendationVisitable(model, it) }
    }

    private fun recommendationVisitable(
        model: RecommendationWidgetModel,
        widget: RecommendationWidget,
    ): RecommendationVisitable =
        if (widget.layoutType == TYPE_COMPARISON_BPC_WIDGET) {
            RecommendationComparisonBpcModel.from(
                metadata = model.metadata,
                trackingModel = model.trackingModel,
                recommendationWidget = widget,
            )
        } else if (widget.layoutType == TYPE_LIMIT_VERTICAL) {
            RecommendationVerticalModel.from(
                metadata = model.metadata,
                trackingModel = model.trackingModel,
                recommendationWidget = widget,
            )
        } else {
            RecommendationCarouselModel.from(
                metadata = model.metadata,
                trackingModel = model.trackingModel,
                widget = widget,
                source = model.source,
            )
        }

    fun loading(model: RecommendationWidgetModel): RecommendationWidgetState = copy(
        widgetMap = widgetMap + mapOf(
            model.id to listOf(RecommendationCarouselShimmeringModel.from(model.metadata))
        )
    )

    fun error(model: RecommendationWidgetModel): RecommendationWidgetState = copy(
        widgetMap = widgetMap + mapOf(model.id to emptyList())
    )

    fun clear() = copy(
        widgetMap = mapOf()
    )

    fun contains(model: RecommendationWidgetModel): Boolean =
        widgetMap.contains(model.id)

    fun refreshMiniCart(
        miniCartData: MiniCartSimplifiedData,
        successMessage: String? = null,
    ): RecommendationWidgetState = copy(
        widgetMap = widgetMap.mapValues { updateQuantityRecommendationVisitable(miniCartData, it) },
        miniCartData = miniCartData,
        successMessage = successMessage,
    )

    private fun updateQuantityRecommendationVisitable(
        miniCartData: MiniCartSimplifiedData,
        widgetMapEntry: Map.Entry<String, List<RecommendationVisitable>>,
    ) = widgetMapEntry.value.map { recommendationVisitable ->
            if (recommendationVisitable is RecommendationCarouselModel)
                recommendationVisitable.copy(
                    widget = updateQuantityRecommendationWidget(recommendationVisitable, miniCartData)
                )
            else
                recommendationVisitable
        }

    private fun updateQuantityRecommendationWidget(
        recommendationCarouselModel:RecommendationCarouselModel,
        miniCartData: MiniCartSimplifiedData,
    ) = recommendationCarouselModel.widget.copy(
        recommendationItemList = recommendationCarouselModel.widget.recommendationItemList.map {
            updateQuantityRecommendationItem(it, miniCartData)
        }
    )

    private fun updateQuantityRecommendationItem(
        recommendationItem: RecommendationItem,
        miniCartData: MiniCartSimplifiedData
    ) = recommendationItem.copy(
        quantity = miniCartData.getQuantity(recommendationItem.productId.toString())
    )

    private fun MiniCartSimplifiedData.getQuantity(productId: String): Int =
        miniCartItems.getMiniCartItemProduct(productId)?.quantity ?: 0

    fun getMiniCartItemProduct(productId: String): MiniCartItem.MiniCartItemProduct? =
        miniCartData?.miniCartItems?.getMiniCartItemProduct(productId)

    fun showErrorMessage(message: String) = copy(errorMessage = message)

    fun dismissMessage() = copy(
        successMessage = null,
        errorMessage = null,
    )
}
