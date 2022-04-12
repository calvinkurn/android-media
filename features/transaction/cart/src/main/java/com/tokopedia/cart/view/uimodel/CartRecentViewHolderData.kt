package com.tokopedia.cart.view.uimodel

/**
 * Created by Irfan Khoirul on 2019-05-31.
 */

data class CartRecentViewHolderData(
        var hasSentImpressionAnalytics: Boolean = false,
        var lastFocussPosition: Int = 0,
        var recentViewList: List<CartRecentViewItemHolderData> = arrayListOf()
)