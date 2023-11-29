package com.tokopedia.search.result.product.dialog

interface BottomSheetInappropriateView {
    fun openInappropriateWarningBottomSheet(
        isAdult: Boolean,
        onButtonConfirmationClicked: () -> Unit = {},
    )
}
