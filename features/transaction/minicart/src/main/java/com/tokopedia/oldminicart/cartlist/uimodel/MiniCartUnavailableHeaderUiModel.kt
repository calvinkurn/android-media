package com.tokopedia.oldminicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.oldminicart.cartlist.adapter.MiniCartListAdapterTypeFactory

data class MiniCartUnavailableHeaderUiModel(
        var unavailableItemCount: Int = 0
) : Visitable<MiniCartListAdapterTypeFactory> {

    override fun type(typeFactory: MiniCartListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}