package com.tokopedia.product.detail.view.viewholder.review.event

import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.view.componentization.ComponentEvent
import com.tokopedia.product.detail.view.viewholder.review.ui.ReviewRatingUiModel

sealed interface ReviewComponentEvent : ComponentEvent {

    data class OnKeywordClicked(
        val keyword: String,
        val trackerData: ComponentTrackDataModel,
        val keywordAmount: Int
    ) : ReviewComponentEvent

    data class OnKeywordImpressed(
        val trackerData: ComponentTrackDataModel,
        val keyword: ReviewRatingUiModel.Keyword,
        val keywordAmount: Int
    ) : ReviewComponentEvent
}
