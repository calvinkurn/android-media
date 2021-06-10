package com.tokopedia.tokomart.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable

data class HomeLayoutListUiModel(
    val result: List<Visitable<*>>,
    val isInitialLoad: Boolean = false,
    val isLoadState: Boolean = false,
    val state: Int = 0
)