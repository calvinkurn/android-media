package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class HomeLayoutListUiModel(
        val items: List<HomeLayoutItemUiModel>,
        @TokoNowLayoutState val state: Int = 0,
        val nextItemIndex: Int? = 0,
        val isInitialLoad: Boolean = false,
        val isLoadDataFinished: Boolean = true
)