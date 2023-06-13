package com.tokopedia.mvc.util.extension

import com.tokopedia.mvc.domain.entity.enums.PromoType

fun PromoType.isCashback(): Boolean {
    return this == PromoType.CASHBACK
}

fun PromoType.isFreeShipping(): Boolean {
    return this == PromoType.FREE_SHIPPING
}

fun PromoType.isDiscount(): Boolean {
    return this == PromoType.DISCOUNT
}
