package com.tokopedia.tokopedianow.buyercomm.presentation.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShipmentOptionData(
    val index: Int = 0,
    val name: String = "",
    val details: List<String> = emptyList(),
    val available: Boolean = true
): Parcelable
