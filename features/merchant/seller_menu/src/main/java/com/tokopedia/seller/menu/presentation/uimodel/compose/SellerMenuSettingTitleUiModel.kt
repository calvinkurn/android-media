package com.tokopedia.seller.menu.presentation.uimodel.compose

import androidx.annotation.DimenRes
import androidx.annotation.StringRes

data class SellerMenuSettingTitleUiModel(
    @StringRes val titleRes: Int,
    @StringRes val ctaRes: Int?,
    @DimenRes val dimenRes: Int
): SellerMenuComposeItem {

    override val itemType: String
        get() = this::class.java.name

}
