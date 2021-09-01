package com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class RepurchaseLayoutUiModel(
    val layoutList: List<Visitable<*>>,
    val nextPage: Int,
    @TokoNowLayoutState val state: Int
)
