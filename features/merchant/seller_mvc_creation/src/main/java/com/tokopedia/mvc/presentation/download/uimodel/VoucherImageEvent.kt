package com.tokopedia.mvc.presentation.download.uimodel

sealed class VoucherImageEvent {
    data class PopulateInitialData(
        val voucherId: Long,
        val voucherImages: List<VoucherImageUiModel>
    ) : VoucherImageEvent()
    data class AddImageToSelection(val imageUrl: String) : VoucherImageEvent()
    data class TapDropdownIcon(val imageUrl: String): VoucherImageEvent()
    data class RemoveImageFromSelection(val imageUrl: String) : VoucherImageEvent()
    object TapDownloadButton : VoucherImageEvent()
}
