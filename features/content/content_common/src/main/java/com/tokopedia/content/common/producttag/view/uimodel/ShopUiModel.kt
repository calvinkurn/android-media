package com.tokopedia.content.common.producttag.view.uimodel

import com.tokopedia.iconunify.IconUnify

/**
 * Created By : Jonathan Darwin on May 11, 2022
 */
data class ShopUiModel(
    val shopId: String = "",
    val shopName: String = "",
    val shopImage: String = "",
    val shopLocation: String = "",
    val shopGoldShop: Int = 0,
    val shopStatus: Int = 0,
    val isOfficial: Boolean = false,
    val isPMPro: Boolean = false,
) {
    val isPM: Boolean
        get() = shopGoldShop == KEY_SHOP_GOLD_SHOP

    val isClosed: Boolean
        get() = shopStatus == KEY_SHOP_STATUS_CLOSED

    val isModerated: Boolean
        get() = shopStatus == KEY_SHOP_STATUS_MODERATED

    val isInactive: Boolean
        get() = shopStatus == KEY_SHOP_STATUS_INACTIVE

    val isShopAccessible: Boolean
        get() = isClosed.not() && isModerated.not() && isInactive.not()

    val isShopHasBadge: Boolean
        get() = badge != KEY_NO_BADGE

    val badge: Int
        get() = when {
            isOfficial -> IconUnify.BADGE_OS_FILLED
            isPMPro -> IconUnify.BADGE_PMPRO_FILLED
            isPM -> IconUnify.BADGE_PM_FILLED
            else -> KEY_NO_BADGE
        }

    companion object {
        private const val KEY_NO_BADGE = 0
        private const val KEY_SHOP_GOLD_SHOP = 1
        private const val KEY_SHOP_STATUS_CLOSED = 2
        private const val KEY_SHOP_STATUS_MODERATED = 3
        private const val KEY_SHOP_STATUS_INACTIVE = 4
    }
}