package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OntimeDelivery(
    var available: Boolean = false,
    val textLabel: String,
    val textDetail: String,
    val urlDetail: String,
    var value: Int = 0,
    var iconUrl: String = "",
    val textUrl: String = ""
) : Parcelable
