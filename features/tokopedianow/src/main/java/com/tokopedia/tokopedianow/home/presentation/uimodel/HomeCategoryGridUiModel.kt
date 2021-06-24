package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.home.constant.HomeLayoutState
import com.tokopedia.tokopedianow.home.presentation.adapter.TokoMartHomeTypeFactory

data class HomeCategoryGridUiModel(
    val id: String,
    val title: String,
    val categoryList: List<HomeCategoryItemUiModel>?,
    @HomeLayoutState val state: Int
): TokoMartHomeLayoutUiModel(id) {
    override fun type(typeFactory: TokoMartHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}