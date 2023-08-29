package com.tokopedia.seller.menu.presentation.uimodel.compose

import androidx.annotation.StringRes

data class SellerMenuItemUiModel(
    @StringRes val titleRes: Int,
    val type: String,
    val eventActionSuffix: String,
    val iconUnifyType: Int
): SellerMenuComposeItem {

    override val itemType: String
        get() = this::class.java.name

}
