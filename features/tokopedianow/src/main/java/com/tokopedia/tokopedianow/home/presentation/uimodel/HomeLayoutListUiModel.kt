package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.home.constant.HomeLayoutState

data class HomeLayoutListUiModel(
    val result: List<Visitable<*>>,
    val isInitialLoad: Boolean = false,
    val isLoadState: Boolean = false,
    @HomeLayoutState val state: Int = 0
)