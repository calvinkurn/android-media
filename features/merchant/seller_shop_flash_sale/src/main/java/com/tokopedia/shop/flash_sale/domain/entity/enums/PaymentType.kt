package com.tokopedia.shop.flash_sale.domain.entity.enums

private const val PAYMENT_TYPE_INSTANT = 0
private const val PAYMENT_TYPE_REGULAR = 1

enum class PaymentType(val id : Int) {
    INSTANT(PAYMENT_TYPE_INSTANT),
    REGULAR(PAYMENT_TYPE_REGULAR),
}