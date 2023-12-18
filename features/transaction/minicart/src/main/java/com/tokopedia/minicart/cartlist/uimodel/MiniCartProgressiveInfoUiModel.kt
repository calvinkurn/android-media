package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory

data class MiniCartProgressiveInfoUiModel(
    val message: String,
    val icon: String,
    val appLink: String
) : Visitable<MiniCartListAdapterTypeFactory> {
    override fun type(typeFactory: MiniCartListAdapterTypeFactory): Int = typeFactory.type(this)
}
