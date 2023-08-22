package com.tokopedia.promousage.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PromoPageEntryPoint : Parcelable {
    CART_PAGE,
    OCC_PAGE,
    CHECKOUT_PAGE
}
