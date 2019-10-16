package com.tokopedia.purchase_platform.features.cart.view.viewmodel

/**
 * Created by Irfan Khoirul on 2019-05-31.
 */

data class CartRecentViewHolderData(
        var lastFocussPosition: Int = 0,
        var recentViewList: List<CartRecentViewItemHolderData> = arrayListOf()
)