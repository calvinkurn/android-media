package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationWidget
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.PRODUCT_RECOM

data class HomeRealTimeRecomUiModel(
    val channelId: String = "",
    val headerName: String = "",
    val parentProductId: String = "",
    val productImageUrl: String = "",
    val warehouseId: String = "",
    val category: String = "",
    val widget: RecommendationWidget? = null,
    val pageName: String = "",
    val enabled: Boolean = false,
    val widgetState: RealTimeRecomWidgetState = RealTimeRecomWidgetState.IDLE,
    val carouselState: Int = RecommendationCarouselData.STATE_LOADING,
    @TokoNowLayoutType val type: String = PRODUCT_RECOM
) {

    val impressHolder = ImpressHolder()

    enum class RealTimeRecomWidgetState {
        IDLE,
        LOADING,
        READY,
        REFRESH
    }

    fun shouldRefresh(channelId: String, productId: String): Boolean {
        return this.channelId == channelId && this.parentProductId != productId && enabled
    }

    fun shouldFetch(): Boolean {
        val productList = widget?.recommendationItemList.orEmpty()
        return productList.isEmpty() && enabled
    }
}
