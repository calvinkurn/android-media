package com.tokopedia.tokopedianow.common.model

import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowTypeFactory

data class TokoNowChooseAddressWidgetUiModel(
        val id: String,
        val isRefresh: Boolean = false,
        val isMyShop: Boolean = false
) : TokoNowLayoutUiModel(id)  {
    override fun type(typeFactory: TokoNowTypeFactory): Int {
        return typeFactory.type(this)
    }
}