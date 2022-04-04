package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.seller.menu.presentation.adapter.SellerMenuTypeFactory

data class ShopProductUiModel(
    val count: Int = Int.ZERO,
    val isShopOwner: Boolean = false
) : Visitable<SellerMenuTypeFactory> {

    override fun type(typeFactory: SellerMenuTypeFactory): Int =
        typeFactory.type(this)

}