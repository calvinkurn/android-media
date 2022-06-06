package com.tokopedia.tokofood.feature.home.presentation.uimodel

import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.domain.data.DynamicIcon
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodHomeTypeFactory

data class TokoFoodHomeIconsUiModel(
    val id: String,
    val widgetParam: String = "",
    val listIcons: List<DynamicIcon>?,
    @TokoFoodLayoutState val state: Int
): TokoFoodHomeLayoutUiModel(id) {
    override fun type(typeFactory: TokoFoodHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}