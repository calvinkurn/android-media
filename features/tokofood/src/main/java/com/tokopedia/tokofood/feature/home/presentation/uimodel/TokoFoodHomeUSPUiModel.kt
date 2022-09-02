package com.tokopedia.tokofood.feature.home.presentation.uimodel

import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState
import com.tokopedia.tokofood.feature.home.domain.data.TokoFoodHomeUSPResponse
import com.tokopedia.tokofood.feature.home.presentation.adapter.TokoFoodHomeTypeFactory

data class TokoFoodHomeUSPUiModel(
    val id: String,
    val uspModel: TokoFoodHomeUSPResponse?,
    @TokoFoodLayoutState val state: Int,
    val verticalPosition: Int,
): TokoFoodHomeLayoutUiModel(id) {
    override fun type(typeFactory: TokoFoodHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}