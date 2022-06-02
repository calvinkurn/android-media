package com.tokopedia.tokofood.feature.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutItemState

data class TokoFoodItemUiModel (
    val layout: Visitable<*>?,
    val state: TokoFoodLayoutItemState
)