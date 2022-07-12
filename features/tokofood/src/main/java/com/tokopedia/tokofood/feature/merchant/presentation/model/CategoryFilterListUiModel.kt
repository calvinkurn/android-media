package com.tokopedia.tokofood.feature.merchant.presentation.model

data class CategoryFilterListUiModel(
    val categoryUiModel: CategoryUiModel,
    val totalMenu: String,
    var isSelected: Boolean
)