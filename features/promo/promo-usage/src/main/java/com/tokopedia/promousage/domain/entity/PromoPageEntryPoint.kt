package com.tokopedia.promousage.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PromoPageEntryPoint : Parcelable {
    CART_PAGE,
    ONE_CLICK_CHECKOUT_PAGE,
    CHECKOUT_PAGE
}
