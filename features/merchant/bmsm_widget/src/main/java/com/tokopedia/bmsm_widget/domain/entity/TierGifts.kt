package com.tokopedia.bmsm_widget.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TierGifts(
    val gifts: List<GiftProduct>,
    val tierId: Long
) : Parcelable {
    @Parcelize
    data class GiftProduct(
        val productId: Long,
        val quantity: Int
    ) : Parcelable
}
