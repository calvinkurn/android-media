package com.tokopedia.promousage.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class PromoItemState : Parcelable {

    object Loading : PromoItemState()

    data class Normal(
        val useSecondaryPromo: Boolean
    ) : PromoItemState()

    data class Selected(
        val useSecondaryPromo: Boolean
    ) : PromoItemState()

    data class Disabled(
        val useSecondaryPromo: Boolean,
        val message: String
    ) : PromoItemState()

    data class Ineligible(val message: String) : PromoItemState()
}
