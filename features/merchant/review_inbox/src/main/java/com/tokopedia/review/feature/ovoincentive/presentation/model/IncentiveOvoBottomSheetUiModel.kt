package com.tokopedia.review.feature.ovoincentive.presentation.model

import com.tokopedia.review.feature.ovoincentive.data.ProductRevIncentiveOvoDomain
import com.tokopedia.review.feature.ovoincentive.data.TncBottomSheetTrackerData

data class IncentiveOvoBottomSheetUiModel(
    val productRevIncentiveOvoDomain: ProductRevIncentiveOvoDomain,
    val trackerData: TncBottomSheetTrackerData? = null,
    val category: String = ""
)
