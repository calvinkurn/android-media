package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant.OOC_TOKONOW
import com.tokopedia.recommendation_widget_common.widget.carousel.RecommendationCarouselData
import com.tokopedia.tokopedianow.common.model.TokoNowRecommendationCarouselUiModel
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLayoutItemUiModel

object ProductRecomOocMapper {
    fun mapResponseToProductRecomOoc(
        state: HomeLayoutItemState
    ): HomeLayoutItemUiModel {
        val productRecomUiModel = TokoNowRecommendationCarouselUiModel(
            pageName = OOC_TOKONOW,
            carouselData = RecommendationCarouselData(
                state = RecommendationCarouselData.STATE_LOADING
            )
        )
        return HomeLayoutItemUiModel(productRecomUiModel, state)
    }
}
