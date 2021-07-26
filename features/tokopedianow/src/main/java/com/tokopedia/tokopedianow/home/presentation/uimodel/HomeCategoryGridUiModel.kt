package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.home.constant.HomeLayoutState
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomeCategoryGridUiModel(
    val id: String,
    val title: String,
    val categoryList: List<HomeCategoryItemUiModel>?,
    @HomeLayoutState val state: Int
): HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}