package com.tokopedia.tokomart.categorylist.presentation.uimodel

data class CategoryListItemUiModel(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val appLinks: String? = null,
    val childList: List<CategoryListChildUiModel> = emptyList()
)