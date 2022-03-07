package com.tokopedia.recommendation_widget_common.presentation.model

import com.tokopedia.minicart.common.domain.data.MiniCartSimplifiedData

/**
 * Created by yfsx on 08/10/21.
 */
data class RecomMinicartWrapperData(
    val pageName: String,
    val miniCartSimplifiedData: MiniCartSimplifiedData
)