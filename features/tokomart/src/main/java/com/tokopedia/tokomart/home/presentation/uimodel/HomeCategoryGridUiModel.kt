package com.tokopedia.tokomart.home.presentation.uimodel

import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeTypeFactory

data class HomeCategoryGridUiModel(
    val id: String,
    val title: String,
    val categoryList: List<HomeCategoryItemUiModel>
): HomeLayoutUiModel {
    override fun type(typeFactory: TokoMartHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}