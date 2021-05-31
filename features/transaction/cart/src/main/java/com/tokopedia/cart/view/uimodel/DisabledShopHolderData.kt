package com.tokopedia.cart.view.uimodel

/**
 * Created by Irfan Khoirul on 2019-10-16.
 */

data class DisabledShopHolderData(
        var shopId: String = "",
        var shopName: String = "",
        var shopBadgeUrl: String = "",
        var isFulfillment: Boolean = false,
        var showDivider: Boolean = false,
        var isTokoNow: Boolean = false
)