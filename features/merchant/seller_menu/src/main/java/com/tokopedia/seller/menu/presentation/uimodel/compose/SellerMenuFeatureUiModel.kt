package com.tokopedia.seller.menu.presentation.uimodel.compose

object SellerMenuFeatureUiModel : SellerMenuComposeItem {
    override val itemType: String
        get() = this::class.java.name

    override val key: String
        get() = this::class.java.name
}
