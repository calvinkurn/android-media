package com.tokopedia.mvc.domain.entity.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PromoType(val id: Int) : Parcelable {
    FREE_SHIPPING(1),
    CASHBACK(2),
    DISCOUNT(3)
}
