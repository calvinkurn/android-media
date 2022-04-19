package com.tokopedia.recommendation_widget_common.presentation.model

/**
 * Created by yfsx on 05/10/21.
 */
data class RecomErrorModel(
    val pageName: String = "",
    val throwable: Throwable = Throwable()
)