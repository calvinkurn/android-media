package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class HomeLayoutListUiModel(
        val items: List<Visitable<*>>,
        @TokoNowLayoutState val state: Int = 0
)