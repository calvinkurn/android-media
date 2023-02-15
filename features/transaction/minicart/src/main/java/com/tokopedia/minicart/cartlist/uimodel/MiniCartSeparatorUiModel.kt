package com.tokopedia.minicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.cartlist.adapter.MiniCartListAdapterTypeFactory

data class MiniCartSeparatorUiModel(
    var height: Int = 0
) : Visitable<MiniCartListAdapterTypeFactory> {

    companion object {
        const val DEFAULT_SEPARATOR_HEIGHT = 4
    }

    override fun type(typeFactory: MiniCartListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
