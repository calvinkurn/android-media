package com.tokopedia.tokopedianow.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowRepurchaseTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState

data class TokoNowRepurchaseUiModel(
    val id: String,
    val title: String = "",
    val productList: List<TokoNowProductCardUiModel>,
    @TokoNowLayoutState val state: Int
): Visitable<TokoNowRepurchaseTypeFactory> {
    override fun type(typeFactory: TokoNowRepurchaseTypeFactory): Int {
        return typeFactory.type(this)
    }
}