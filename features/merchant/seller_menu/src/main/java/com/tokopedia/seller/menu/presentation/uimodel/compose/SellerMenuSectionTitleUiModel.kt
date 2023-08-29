package com.tokopedia.seller.menu.presentation.uimodel.compose

import androidx.annotation.StringRes

data class SellerMenuSectionTitleUiModel(
    @StringRes val titleRes: Int
): SellerMenuComposeItem {

    override val itemType: String
        get() = this::class.java.name

}
