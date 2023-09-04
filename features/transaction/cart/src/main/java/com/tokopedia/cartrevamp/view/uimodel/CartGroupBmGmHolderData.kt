package com.tokopedia.cartrevamp.view.uimodel

data class CartGroupBmGmHolderData(
    var hasBmGmOffer: Boolean = false,
    var discountBmGmAmount: Double = 0.0,
    var offerId: Long = 0L,
    var offerJsonData: String = "",
    var cartBmGmGroupTickerCartString: String = "",
    var bundleId: Long = 0L,
    var bundleGroupId: String = ""
)
