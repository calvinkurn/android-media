package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.home.constant.HomeLayoutState

data class HomeLayoutListUiModel(
        val result: List<HomeLayoutItemUiModel>,
        @HomeLayoutState val state: Int = 0,
        val nextItemIndex: Int = 0,
        val isInitialLoad: Boolean = false,
        val isInitialLoadFinished: Boolean = false
)