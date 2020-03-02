package com.tokopedia.product.manage.feature.filter.presentation.adapter.viewmodel

data class FilterDataViewModel(
        val id: String,
        val name: String,
        val values: String = "",
        val select: Boolean = false
)