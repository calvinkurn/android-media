package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.seller.menu.presentation.adapter.SellerMenuTypeFactory

object ShopInfoLoadingUiModel : SellerMenuItem {

    override fun type(typeFactory: SellerMenuTypeFactory): Int {
        return typeFactory.type(this)
    }
}
