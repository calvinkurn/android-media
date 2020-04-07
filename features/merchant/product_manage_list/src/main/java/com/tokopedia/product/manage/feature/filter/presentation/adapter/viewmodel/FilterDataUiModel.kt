package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel

data class FilterDataUiModel(
        val id: String,
        val name: String = "",
        val value: String = "",
        var select: Boolean = false
)