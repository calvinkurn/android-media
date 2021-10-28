package com.tokopedia.checkout.old.domain.model.checkout

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrackerData(
        var productChangesType: String = "",
        var campaignType: String = "",
        var productIds: List<String> = emptyList()
) : Parcelable