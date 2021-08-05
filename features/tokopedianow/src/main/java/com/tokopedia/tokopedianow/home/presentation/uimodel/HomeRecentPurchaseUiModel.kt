package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomeRecentPurchaseUiModel(
    val id: String,
    val title: String = "",
    val productList: List<HomeProductCardUiModel>,
    @TokoNowLayoutState val state: Int
): HomeLayoutUiModel(id) {
    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}