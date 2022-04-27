package com.tokopedia.oldminicart.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.oldminicart.cartlist.adapter.MiniCartListAdapterTypeFactory

data class MiniCartUnavailableReasonUiModel(
        var reason: String = "",
        var description: String = ""
) : Visitable<MiniCartListAdapterTypeFactory> {

    override fun type(typeFactory: MiniCartListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}