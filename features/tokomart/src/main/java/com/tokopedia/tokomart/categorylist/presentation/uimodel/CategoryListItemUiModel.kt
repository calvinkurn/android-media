package com.tokopedia.tokomart.categorylist.presentation.uimodel

import com.tokopedia.tokomart.categorylist.presentation.adapter.TokoMartCategoryListTypeFactory

data class CategoryListItemUiModel(
    val id: String,
    val title: String,
    val iconUrl: String? = null,
    val childList: List<CategoryListChildUiModel> = emptyList()
) : CategoryListUiModel() {
    override fun type(typeFactory: TokoMartCategoryListTypeFactory): Int {
        return typeFactory.type(this)
    }
}