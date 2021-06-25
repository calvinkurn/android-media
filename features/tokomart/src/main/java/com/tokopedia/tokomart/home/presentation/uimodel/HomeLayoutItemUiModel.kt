package com.tokopedia.tokomart.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.home.constant.HomeLayoutItemState

data class HomeLayoutItemUiModel(
    val layout: Visitable<*>,
    val state: HomeLayoutItemState
)