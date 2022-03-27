package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.seller.menu.presentation.adapter.SellerMenuTypeFactory

data class ShopProductUiModel(
    val count: Int = 0,
    val isShopOwner: Boolean = false
) : Visitable<SellerMenuTypeFactory> {

    override fun type(typeFactory: SellerMenuTypeFactory): Int =
        typeFactory.type(this)

}