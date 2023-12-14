package com.tokopedia.search.result.product.dialog

import com.tokopedia.search.result.presentation.model.ProductItemDataView

interface BottomSheetInappropriateView {
    fun openInappropriateWarningBottomSheet(
        item: ProductItemDataView,
        isAdult: Boolean,
        onButtonConfirmationClicked: () -> Unit = {},
    )
}
