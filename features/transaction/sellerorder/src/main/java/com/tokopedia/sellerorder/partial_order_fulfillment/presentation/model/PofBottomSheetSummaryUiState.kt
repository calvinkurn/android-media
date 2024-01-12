package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model

import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofVisitable

sealed interface PofBottomSheetSummaryUiState {
    object Hidden : PofBottomSheetSummaryUiState
    data class Showing(
        val items: List<PofVisitable>
    ) : PofBottomSheetSummaryUiState
}
