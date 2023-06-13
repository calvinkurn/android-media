package com.tokopedia.checkout.view.uimodel

data class ShipmentUpsellModel(
    var isShow: Boolean = false,
    var title: String = "",
    var description: String = "",
    var appLink: String = "",
    var image: String = "",
    var hasSeenUpsell: Boolean = false // flag for impression tracker
)
