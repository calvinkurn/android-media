package com.tokopedia.tokomart.home.presentation.uimodel

import com.tokopedia.tokomart.home.presentation.adapter.TokoMartHomeTypeFactory

data class HomeChooseAddressWidgetUiModel(
        val isRefresh: Boolean = false,
        val isMyShop: Boolean = false
) : HomeLayoutUiModel  {
    override fun type(typeFactory: TokoMartHomeTypeFactory): Int {
        return typeFactory.type(this)
    }
}