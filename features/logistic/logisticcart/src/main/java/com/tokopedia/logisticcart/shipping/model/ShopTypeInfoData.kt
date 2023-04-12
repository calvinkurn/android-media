package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ShopTypeInfoData(
    val shopTier: Int = 0,
    val shopGrade: Int = 0,
    val shopBadge: String = "",
    val badgeSvg: String = "",
    val title: String = "",
    val titleFmt: String = "",

    // Temporary field to store shop type to be sent as dimension81
    // Need to remove in the future when implementing tracking for PM Pro
    val shopType: String = ""
) : Parcelable
