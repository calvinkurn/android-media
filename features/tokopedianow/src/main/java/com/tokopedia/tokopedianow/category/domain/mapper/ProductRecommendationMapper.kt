package com.tokopedia.tokopedianow.category.domain.mapper

import com.tokopedia.discovery.common.constants.SearchApiConst
import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.recommendation_widget_common.viewutil.RecomPageConstant
import com.tokopedia.tokopedianow.category.presentation.util.CategoryLayoutType
import com.tokopedia.tokopedianow.common.domain.usecase.GetTargetedTickerUseCase
import com.tokopedia.tokopedianow.common.model.TokoNowProductRecommendationUiModel

object ProductRecommendationMapper {
    fun createProductRecommendation(categoryIds: List<String>) = TokoNowProductRecommendationUiModel(
        id = CategoryLayoutType.PRODUCT_RECOMMENDATION.name,
        requestParam = createRequestParam(categoryIds),
        tickerPageSource = GetTargetedTickerUseCase.CATEGORY_PAGE
    )

    fun createRequestParam(categoryIds: List<String>): GetRecommendationRequestParam {
        return GetRecommendationRequestParam(
            queryParam = RecomPageConstant.TOKONOW_CLP,
            pageName = RecomPageConstant.TOKONOW_CLP,
            categoryIds = categoryIds,
            xSource = RecomPageConstant.RECOM_WIDGET,
            isTokonow = true,
            pageNumber = RecomPageConstant.PAGE_NUMBER_RECOM_WIDGET,
            xDevice = SearchApiConst.DEFAULT_VALUE_OF_PARAMETER_DEVICE
        )
    }
}
