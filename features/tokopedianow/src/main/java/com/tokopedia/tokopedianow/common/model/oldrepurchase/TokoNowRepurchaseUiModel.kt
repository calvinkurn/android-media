package com.tokopedia.tokopedianow.common.model.oldrepurchase

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.oldrepurchase.TokoNowRepurchaseTypeFactory
import com.tokopedia.tokopedianow.common.constant.TokoNowLayoutState
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardUiModel

data class TokoNowRepurchaseUiModel(
    val id: String,
    val title: String = "",
    val productList: List<TokoNowProductCardUiModel>,
    @TokoNowLayoutState val state: Int
) : Visitable<TokoNowRepurchaseTypeFactory> {
    override fun type(typeFactory: TokoNowRepurchaseTypeFactory): Int {
        return typeFactory.type(this)
    }
}
