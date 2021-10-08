package com.tokopedia.recommendation_widget_common.presentation.model

/**
 * Created by yfsx on 08/10/21.
 */
data class RecomItemTrackingMetadata(
    val categoryIds: List<String> = listOf(),
    val keyword: String = "",
    var cartId: String = ""
)