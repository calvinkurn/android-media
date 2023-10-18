package com.tokopedia.logisticcart.shipping.model

// interface for ShipmentCartItems, consists of:
// 1. ShipmentCartItemTopModel
// 2. CartItemModel (for each product)
// 3. CartItemExpandModel (optional, only if has more than 1 cart item)
// 4. ShipmentCartItemModel
sealed interface ShipmentCartItem {
    val cartStringGroup: String
}
