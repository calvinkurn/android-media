package com.tokopedia.promousage.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class PromoItemState : Parcelable {

    open val useSecondaryPromo: Boolean = false

    data class Loading(
        override val useSecondaryPromo: Boolean
    ) : PromoItemState()

    data class Normal(
        override val useSecondaryPromo: Boolean
    ) : PromoItemState()

    data class Selected(
        override val useSecondaryPromo: Boolean
    ) : PromoItemState()

    data class Disabled(
        override val useSecondaryPromo: Boolean,
        val message: String
    ) : PromoItemState()

    data class Ineligible(
        override val useSecondaryPromo: Boolean = false,
        val message: String
    ) : PromoItemState()
}
