package com.tokopedia.minicart.ui.cartlist.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.minicart.ui.cartlist.adapter.MiniCartListAdapterTypeFactory

class MiniCartProductUiModel : Visitable<MiniCartListAdapterTypeFactory> {

    override fun type(typeFactory: MiniCartListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}