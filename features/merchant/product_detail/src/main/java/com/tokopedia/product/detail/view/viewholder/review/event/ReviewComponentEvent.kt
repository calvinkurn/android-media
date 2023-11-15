package com.tokopedia.product.detail.view.viewholder.review.event

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.fragment.delegate.BaseComponentEvent

sealed interface ReviewComponentEvent : BaseComponentEvent

data class OnKeywordClicked(
    val keyword: String,
    val trackerData: ComponentTrackDataModel,
    val keywordAmount: Int
) : ReviewComponentEvent
