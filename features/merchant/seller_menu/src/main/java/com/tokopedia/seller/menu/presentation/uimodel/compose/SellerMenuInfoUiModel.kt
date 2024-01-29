package com.tokopedia.seller.menu.presentation.uimodel.compose

import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.nest.principles.utils.ImpressionHolder
import com.tokopedia.seller.menu.common.view.uimodel.UserShopInfoWrapper

data class SellerMenuInfoUiModel(
    val shopAvatarUrl: String = String.EMPTY,
    val shopScore: Long = 0,
    val shopName: String = String.EMPTY,
    val shopAge: Long = 0,
    val shopFollowers: Long = 0,
    val shopBadgeUrl: String? = null,
    val userShopInfoWrapper: UserShopInfoWrapper,
    val partialResponseStatus: Pair<Boolean, Boolean>,
    val balanceValue: String = String.EMPTY,
    val impressHolder: ImpressionHolder = ImpressionHolder(),
    val shopScoreImpressionHolder: ImpressionHolder = ImpressionHolder()
) : SellerMenuComposeItem {

    override val itemType: String
        get() = this::class.java.name

    override val key: String
        get() = this::class.java.name
}
