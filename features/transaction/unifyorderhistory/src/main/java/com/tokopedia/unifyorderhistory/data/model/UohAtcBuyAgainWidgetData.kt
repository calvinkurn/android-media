package com.tokopedia.unifyorderhistory.data.model

import com.tokopedia.atc_common.domain.model.response.AtcMultiData
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.usecase.coroutines.Result

data class UohAtcBuyAgainWidgetData(
    val recommItem: RecommendationItem,
    val index: Int,
    val atcResponse: Result<AtcMultiData>
)
