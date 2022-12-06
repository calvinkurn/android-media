package com.tokopedia.mvc.presentation.download.uimodel

data class VoucherImageUiState(
    val selectedImageUrls: Set<String> = emptySet(),
    val voucherImages: List<VoucherImageUiModel> = emptyList()
)
