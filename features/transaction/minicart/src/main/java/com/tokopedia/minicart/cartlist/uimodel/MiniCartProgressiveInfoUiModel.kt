package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory

data class MiniCartProgressiveInfoUiModel(
    val offerId: Long,
    val message: String,
    val icon: String,
    val appLink: String,
    val isRefreshLayout: Boolean
) : Visitable<MiniCartListAdapterTypeFactory> {
    override fun type(typeFactory: MiniCartListAdapterTypeFactory): Int = typeFactory.type(this)
}
