package com.tokopedia.paylater.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OfferListResponse(
        val payLaterOfferName: String? = "",
        val offerItemList: List<OfferDescriptionItem> = listOf(OfferDescriptionItem())
): Parcelable