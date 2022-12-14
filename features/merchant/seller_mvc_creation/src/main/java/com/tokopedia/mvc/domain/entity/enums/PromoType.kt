package com.tokopedia.mvc.domain.entity.enums

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PromoType(val id: Int, val text: String) : Parcelable {
    FREE_SHIPPING(1, "shipping"),
    DISCOUNT(2, "discount"),
    CASHBACK(3, "cashback");

    companion object {

        fun mapToString(type: Int): String {
            return values().firstOrNull {
                it.id == type
            }?.text ?: FREE_SHIPPING.text
        }
    }
}
