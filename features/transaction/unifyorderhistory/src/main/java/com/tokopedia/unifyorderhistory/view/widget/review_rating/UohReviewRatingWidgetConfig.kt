package com.tokopedia.unifyorderhistory.view.widget.review_rating

import com.tokopedia.unifyorderhistory.data.model.UohListOrder

data class UohReviewRatingWidgetConfig(
    val show: Boolean = false,
    val componentData: UohListOrder.UohOrders.Order.Metadata.ExtraComponent = UohListOrder.UohOrders.Order.Metadata.ExtraComponent(),
    val onRatingChanged: (appLink: String) -> Unit = {}
)
