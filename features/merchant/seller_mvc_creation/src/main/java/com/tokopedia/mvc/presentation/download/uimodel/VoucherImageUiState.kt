package com.tokopedia.mvc.presentation.download.uimodel

data class VoucherImageUiState(
    val voucherId: Long = 0,
    val selectedImageUrls: List<VoucherImageUiModel> = emptyList(),
    val voucherImages: List<VoucherImageUiModel> = emptyList()
)
