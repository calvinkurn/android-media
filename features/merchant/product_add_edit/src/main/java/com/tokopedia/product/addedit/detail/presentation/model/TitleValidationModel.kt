package com.tokopedia.product.addedit.detail.presentation.model

data class TitleValidationModel (
        var title: String = "",
        var errorKeywords: List<String> = emptyList(),
        var warningKeywords: List<String> = emptyList(),
        var isBlacklistKeyword: Boolean = false,
        var isNegativeKeyword: Boolean = false,
        var isTypoDetected: Boolean = false
)