package com.tokopedia.shop.flashsale.presentation.creation.manage.enums

private const val SHOP_STATUS_OPEN = 1
private const val SHOP_STATUS_CLOSED = 2
private const val SHOP_STATUS_OTHER = 0

enum class ShopStatus(val type: Int) {
    OPEN(SHOP_STATUS_OPEN),
    CLOSED(SHOP_STATUS_CLOSED),
    OTHER(SHOP_STATUS_OTHER)
}