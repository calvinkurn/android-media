package com.tokopedia.mvc.presentation.download.uimodel

sealed class VoucherImageEffect {
    data class DownloadImages(val selectedImageUrls: Set<String>) : VoucherImageEffect()
}
