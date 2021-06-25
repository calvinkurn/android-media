package com.tokopedia.tokomart.home.presentation.uimodel

import com.tokopedia.tokomart.home.constant.HomeLayoutState

data class HomeLayoutListUiModel(
    val result: List<HomeLayoutItemUiModel>,
    @HomeLayoutState val state: Int = 0,
    val nextItemIndex: Int = 0,
    val isInitialLoad: Boolean = false,
    val isInitialLoadFinished: Boolean = false
)