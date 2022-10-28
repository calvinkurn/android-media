package com.tokopedia.tokofood.feature.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokofood.feature.home.domain.constanta.TokoFoodLayoutState

data class TokoFoodListUiModel(
    val items: List<Visitable<*>>,
    @TokoFoodLayoutState val state: Int = 0
)