package com.tokopedia.seller.menu.presentation.uimodel.compose

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper

data class SellerMenuInfoUiModel(
    val shopScore: Long = 0,
    val shopAge: Long = 0,
    val shopFollowers: Long = 0,
    val shopBadgeUrl: String? = null,
    val userShopInfoWrapper: UserShopInfoWrapper,
    val balanceValue: String = String.EMPTY
): SellerMenuComposeItem {

    override val itemType: String
        get() = this::class.java.name

}
