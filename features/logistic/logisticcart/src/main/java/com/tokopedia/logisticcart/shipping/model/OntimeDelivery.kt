package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OntimeDelivery(
        var available: Boolean = false,
        val textLabel: String,
        val textDetail: String,
        val urlDetail: String,
        var value: Int = 0,
        var iconUrl: String = ""
) : Parcelable