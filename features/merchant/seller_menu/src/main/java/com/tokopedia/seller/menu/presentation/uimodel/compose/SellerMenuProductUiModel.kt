package com.tokopedia.seller.menu.presentation.uimodel.compose

import com.tokopedia.kotlin.extensions.view.ZERO

data class SellerMenuProductUiModel(
    val count: Int = Int.ZERO,
    val isShopOwner: Boolean = false
): SellerMenuComposeItem {

    override val itemType: String
        get() = this::class.java.name

}
