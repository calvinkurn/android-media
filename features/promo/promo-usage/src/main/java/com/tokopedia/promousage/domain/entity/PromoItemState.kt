package com.tokopedia.promousage.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class PromoItemState : Parcelable {

    object Loading : PromoItemState()

    object Normal : PromoItemState()

    object Selected : PromoItemState()

    data class Disabled(val message: String) : PromoItemState()

    data class Ineligible(val message: String) : PromoItemState()
}
