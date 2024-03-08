package com.tokopedia.sellerorder.partial_order_fulfillment.presentation.model

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.order_management_common.presentation.uimodel.StringRes
import com.tokopedia.sellerorder.partial_order_fulfillment.presentation.adapter.model.PofVisitable

data class UiState(
    val title: StringRes = StringRes(Int.ZERO),
    val showResetButton: Boolean = false,
    val items: List<PofVisitable> = listOf(),
    val footerUiState: PofFooterUiState = PofFooterUiState.Loading(),
    val bottomSheetSummaryUiState: PofBottomSheetSummaryUiState = PofBottomSheetSummaryUiState.Hidden
)
