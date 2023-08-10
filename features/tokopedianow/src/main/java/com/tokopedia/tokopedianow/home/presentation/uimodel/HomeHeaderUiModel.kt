package com.tokopedia.tokopedianow.home.presentation.uimodel

import com.tokopedia.tokopedianow.home.constant.HomeLayoutItemState
import com.tokopedia.tokopedianow.home.presentation.adapter.HomeTypeFactory

data class HomeHeaderUiModel(
    val id: String,
    val title: String = "",
    val shippingHint: String = "",
    val shopStatus: String = "",
    val logoUrl: String = "",
    val state: HomeLayoutItemState = HomeLayoutItemState.LOADING
) : HomeLayoutUiModel(id) {

    override fun type(typeFactory: HomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}
