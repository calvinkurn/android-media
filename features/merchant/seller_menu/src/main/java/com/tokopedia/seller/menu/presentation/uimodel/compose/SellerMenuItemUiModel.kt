package com.tokopedia.seller.menu.presentation.uimodel.compose

import androidx.annotation.StringRes
import com.tokopedia.kotlin.extensions.view.ZERO

data class SellerMenuItemUiModel(
    @StringRes val titleRes: Int,
    val type: String,
    val eventActionSuffix: String,
    val iconUnifyType: Int,
    val actionClick: SellerMenuActionClick,
    val notificationCount: Int = Int.ZERO
) : SellerMenuComposeItem {

    override val itemType: String
        get() = this::class.java.name

    override val key: String
        get() = this::class.java.name + titleRes
}
