package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutType.Companion.PRODUCT_RECOM
import com.tokopedia.tokopedianow.common.constant.TokoNowProductRecommendationState
import com.tokopedia.productcard_compact.productcardcarousel.presentation.uimodel.TokoNowProductCardCarouselItemUiModel

data class HomeRealTimeRecomUiModel(
    val channelId: String = "",
    val headerName: String = "",
    val parentProductId: String = "",
    val productImageUrl: String = "",
    val warehouseId: String = "",
    val category: String = "",
    val productList: List<TokoNowProductCardCarouselItemUiModel> = emptyList(),
    val pageName: String = "",
    val enabled: Boolean = false,
    val widgetState: RealTimeRecomWidgetState = RealTimeRecomWidgetState.IDLE,
    val carouselState: TokoNowProductRecommendationState = TokoNowProductRecommendationState.LOADING,
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
        return productList.isEmpty() && enabled
    }
}
