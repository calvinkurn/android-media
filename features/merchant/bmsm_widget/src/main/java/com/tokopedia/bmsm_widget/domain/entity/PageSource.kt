package com.tokopedia.bmsm_widget.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class PageSource : Parcelable {
    OFFER_LANDING_PAGE,
    CART
}
