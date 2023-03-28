package com.tokopedia.logisticcart.shipping.model

data class ShipmentCartItemTopModel(
    val isError: Boolean = false,
    val errorTitle: String = "",
    val errorDescription: String = "",
    val isHasUnblockingError: Boolean = false,
    val unblockingErrorMessage: String = "",
    val firstProductErrorIndex: Int = -1,
    val isTriggerScrollToErrorProduct: Boolean = false,
    val isCustomEpharmacyError: Boolean = false,

    // Shop data
    val shopId: Long = 0,
    val shopName: String = "",
    val orderNumber: Int = 0,
    val preOrderInfo: String = "",
    val freeShippingBadgeUrl: String = "",
    val isFreeShippingPlus: Boolean = false, // flag for plus badge tracker
    val hasSeenFreeShippingBadge: Boolean = false, // flag for tracker
    val shopLocation: String = "",
    val shopAlertMessage: String = "",
    val shopTypeInfoData: ShopTypeInfoData = ShopTypeInfoData(),
    val isTokoNow: Boolean = false,
    val shopTickerTitle: String = "",
    val shopTicker: String = "",
    val enablerLabel: String = "",
    val hasTradeInItem: Boolean = false,

    val isFulfillment: Boolean = false,
    val fulfillmentBadgeUrl: String = "",

    val isStateAllItemViewExpanded: Boolean = true,

    override val cartString: String
) : ShipmentCartItem
