package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.seller.menu.presentation.adapter.SellerMenuTypeFactory

object ShopInfoErrorUiModel: Visitable<SellerMenuTypeFactory> {
    override fun type(typeFactory: SellerMenuTypeFactory): Int {
        return typeFactory.type(this)
    }

}