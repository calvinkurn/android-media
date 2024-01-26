package com.tokopedia.seller.menu.presentation.uimodel.compose

import androidx.annotation.StringRes

data class SellerMenuSectionTitleUiModel(
    @StringRes val titleRes: Int
) : SellerMenuComposeItem {

    override val itemType: String
        get() = this::class.java.name

    override val key: String
        get() = this::class.java.name + titleRes
}
