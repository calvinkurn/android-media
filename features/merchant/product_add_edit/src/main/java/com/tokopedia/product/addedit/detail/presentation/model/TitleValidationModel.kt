package com.tokopedia.product.addedit.detail.presentation.model

data class TitleValidationModel (
        val title: String = "",
        val errorKeywords: List<String> = emptyList(),
        val warningKeywords: List<String> = emptyList()
)