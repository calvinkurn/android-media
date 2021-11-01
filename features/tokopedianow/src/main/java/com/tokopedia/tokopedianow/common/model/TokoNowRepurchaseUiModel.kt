package com.tokopedia.tokopedianow.common.model

import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class TokoNowRepurchaseUiModel(
    val id: String,
    val title: String = "",
    val productList: List<TokoNowProductCardUiModel>,
    @TokoNowLayoutState val state: Int
): TokoNowLayoutUiModel(id) {
    override fun type(typeFactory: TokoNowTypeFactory): Int {
        return typeFactory.type(this)
    }
}