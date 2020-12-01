package com.tokopedia.paylater.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OfferDescriptionItem(
        val offerItemPoint: String? = "",
        val isHighlight: Boolean? = false
): Parcelable