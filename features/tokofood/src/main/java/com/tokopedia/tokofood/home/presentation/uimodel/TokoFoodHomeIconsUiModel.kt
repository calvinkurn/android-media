package com.tokopedia.tokofood.home.presentation.uimodel

import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeTypeFactory

data class TokoFoodHomeIconsUiModel(
    val id: String,
    val listIcons: List<TokoFoodIcon>
): TokoFoodHomeLayoutUiModel(id) {
    override fun type(typeFactory: TokoFoodHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class TokoFoodIcon (
    val id : String = "",
    val imageUrl: String = "",
    val textIcon: String = "",
    val applinkUrl: String = ""
)