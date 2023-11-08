package com.tokopedia.seller.menu.presentation.uimodel.compose

import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType

data class SellerMenuDividerUiModel(
    val type: DividerType = DividerType.THIN_PARTIAL,
    val id: String
) : SellerMenuComposeItem {

    override val itemType: String
        get() = this::class.java.name

    override val key: String
        get() = this::class.java.name + id
}
