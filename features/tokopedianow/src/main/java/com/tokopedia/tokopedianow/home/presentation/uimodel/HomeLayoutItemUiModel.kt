package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState

data class HomeLayoutItemUiModel(
    val layout: Visitable<*>,
    val state: HomeLayoutItemState
)