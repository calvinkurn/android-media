package com.tokopedia.tokopedianow.buyercomm.presentation.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentOptionData(
    val name: String = "",
    val detail: String = "",
    val available: Boolean = true
): Parcelable
