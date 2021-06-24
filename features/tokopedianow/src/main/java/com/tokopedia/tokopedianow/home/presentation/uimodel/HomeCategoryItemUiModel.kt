package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.home.presentation.adapter.TokoMartHomeTypeFactory

data class HomeCategoryItemUiModel(
    val id: String,
    val title: String,
    val imageUrl: String?,
    val appLink: String
): TokoMartHomeLayoutUiModel(id) {
    override fun type(typeFactory: TokoMartHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}