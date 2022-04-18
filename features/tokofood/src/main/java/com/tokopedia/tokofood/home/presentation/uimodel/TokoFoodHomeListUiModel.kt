package com.tokopedia.tokofood.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodHomeLayoutState

data class TokoFoodHomeListUiModel(
    val items: List<Visitable<*>> = emptyList(),
    @TokoFoodHomeLayoutState val state: Int = 0
)