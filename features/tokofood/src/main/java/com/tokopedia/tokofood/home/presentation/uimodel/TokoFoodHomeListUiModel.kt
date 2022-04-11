package com.tokopedia.tokofood.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.home.domain.constanta.TokoFoodLayoutState

data class TokoFoodHomeListUiModel(
    val items: List<Visitable<*>>,
    @TokoFoodLayoutState val state: Int = 0
)