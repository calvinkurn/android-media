package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopTypeInfoData(
        var shopTier: Int = 0,
        var shopGrade: Int = 0,
        var shopBadge: String = "",
        var badgeSvg: String = "",
        var title: String = "",
        var titleFmt: String = "",

        // Temporary field to store shop type to be sent as dimension81
        // Need to remove in the future when implementing tracking for PM Pro
        var shopType: String = ""
) : Parcelable