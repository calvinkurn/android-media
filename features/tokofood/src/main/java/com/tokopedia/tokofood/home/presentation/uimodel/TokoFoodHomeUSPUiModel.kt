package com.tokopedia.tokofood.home.presentation.uimodel

import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutState
import com.tokopedia.tokofood.home.domain.data.TokoFoodHomeUSPResponse
import com.tokopedia.tokofood.home.presentation.adapter.TokoFoodHomeTypeFactory

data class TokoFoodHomeUSPUiModel(
    val id: String,
    val uspModel: TokoFoodHomeUSPResponse?,
    @TokoFoodHomeLayoutState val state: Int
): TokoFoodHomeLayoutUiModel(id) {
    override fun type(typeFactory: TokoFoodHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}