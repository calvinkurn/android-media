package com.tokopedia.checkout.view.uimodel

data class ShipmentNewUpsellModel(
    var isShow: Boolean = false,
    var isSelected: Boolean = false,
    var description: String = "",
    var appLink: String = "",
    var image: String = "",
    var price: Long = 0,
    var priceWording: String = "",
    var duration: String = "",
    var summaryInfo: String = "",
    var buttonText: String = "",
    var id: String = "",
    var additionalVerticalId: String = "",
    var transactionType: String = "",
    var hasSeenUpsell: Boolean = false // flag for impression tracker
)
