package com.tokopedia.tokofood.home.presentation.uimodel

import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutState
import com.tokopedia.tokofood.home.domain.data.DynamicIcon
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeTypeFactory

data class TokoFoodHomeIconsUiModel(
    val id: String,
    val widgetParam: String = "",
    val listIcons: List<DynamicIcon>?,
    @TokoFoodHomeLayoutState val state: Int
): TokoFoodHomeLayoutUiModel(id) {
    override fun type(typeFactory: TokoFoodHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}