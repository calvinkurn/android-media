package com.tokopedia.tokopedianow.home.domain.mapper

import com.tokopedia.recommendation_widget_common.domain.request.GetRecommendationRequestParam
import com.tokopedia.tokopedianow.common.constant.ConstantValue
import com.tokopedia.tokopedianow.common.util.QueryParamUtil.getStringValue

object RecomParamMapper {

    private const val PARAM_PAGE_NAME = "pageName"
    private const val PARAM_CATEGORY_IDS = "categoryIDs"

    private const val CATEGORY_IDS_DELIMITER = ","

    fun mapToRecomRequestParam(params: String): GetRecommendationRequestParam {
        val pageName = params.getStringValue(PARAM_PAGE_NAME)
        val categoryIDs = params.getStringValue(PARAM_CATEGORY_IDS)
            .toList(CATEGORY_IDS_DELIMITER)

        return GetRecommendationRequestParam(
            pageName = pageName,
            categoryIds = categoryIDs,
            xSource = ConstantValue.X_SOURCE_RECOMMENDATION_PARAM,
            xDevice = ConstantValue.X_DEVICE_RECOMMENDATION_PARAM,
            isTokonow = true
        )
    }

    private fun String.toList(delimiter: String): List<String> {
        return if (isNotEmpty()) split(delimiter) else emptyList()
    }
}
