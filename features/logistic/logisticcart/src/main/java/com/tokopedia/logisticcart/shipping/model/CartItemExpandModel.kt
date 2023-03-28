package com.tokopedia.logisticcart.shipping.model

data class CartItemExpandModel(
    override val cartString: String,
    val cartSize: Int = 0,
    val isExpanded: Boolean = true
) : ShipmentCartItem
