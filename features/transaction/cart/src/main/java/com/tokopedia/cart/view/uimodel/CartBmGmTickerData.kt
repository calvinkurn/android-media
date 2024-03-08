package com.tokopedia.cart.view.uimodel

data class CartBmGmTickerData(
    var bmGmCartInfoData: CartDetailInfo = CartDetailInfo(),
    var isShowTickerBmGm: Boolean = false,
    // 0 = loading, 1 = active, 2 = inactive
    var stateTickerBmGm: Int = -1,
    var isShowBmGmDivider: Boolean = false,
    var isShowBmGmHorizontalDivider: Boolean = false
)
