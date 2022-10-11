package com.tokopedia.shop.flashsale.domain.entity.enums

const val PAYMENT_TYPE_INSTANT = 0
const val PAYMENT_TYPE_REGULAR = 1

enum class PaymentType(val id : Int) {
    INSTANT(PAYMENT_TYPE_INSTANT),
    REGULAR(PAYMENT_TYPE_REGULAR),
}