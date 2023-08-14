package com.tokopedia.logisticcart.shipping.model

data class ShipmentCartItemTopModel(
    val isError: Boolean = false,
    val errorTitle: String = "",
    val errorDescription: String = "",
    val isHasUnblockingError: Boolean = false,
    val unblockingErrorMessage: String = "",
    val firstProductErrorIndex: Int = -1,
    val isCustomEpharmacyError: Boolean = false,

    // Shop data
    val shopId: Long = 0,
    val shopName: String = "",
    val orderNumber: Int = 0,
    val preOrderInfo: String = "",
    val freeShippingBadgeUrl: String = "",
    val isFreeShippingPlus: Boolean = false, // flag for plus badge tracker
    var hasSeenFreeShippingBadge: Boolean = false, // flag for tracker
    val shopLocation: String = "",
    val shopAlertMessage: String = "",
    val shopTypeInfoData: ShopTypeInfoData = ShopTypeInfoData(),
    val shopTickerTitle: String = "",
    var shopTicker: String = "",
    val enablerLabel: String = "",
    val hasTradeInItem: Boolean = false,

    val isFulfillment: Boolean = false,
    val fulfillmentBadgeUrl: String = "",

    val uiGroupType: Int = 0,
    val groupInfoName: String = "",
    val groupInfoBadgeUrl: String = "",
    val groupInfoDescription: String = "",
    val groupInfoDescriptionBadgeUrl: String = "",

    override val cartStringGroup: String
) : ShipmentCartItem
