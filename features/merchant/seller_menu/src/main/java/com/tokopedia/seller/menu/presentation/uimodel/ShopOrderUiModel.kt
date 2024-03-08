package com.tokopedia.seller.menu.presentation.uimodel

import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.seller.menu.presentation.adapter.SellerMenuTypeFactory

data class ShopOrderUiModel(
    val newOrderCount: Int = Int.ZERO,
    val readyToShip: Int = Int.ZERO,
    val isShopOwner: Boolean = false
) : SellerMenuItem {

    override fun type(typeFactory: SellerMenuTypeFactory): Int = typeFactory.type(this)
}
