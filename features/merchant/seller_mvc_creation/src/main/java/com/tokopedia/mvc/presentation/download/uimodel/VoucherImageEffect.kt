package com.tokopedia.mvc.presentation.download.uimodel

sealed class VoucherImageEffect {
    data class DownloadImages(
        val voucherId: Long,
        val selectedImageUrls: List<VoucherImageUiModel>
    ) : VoucherImageEffect()
}
