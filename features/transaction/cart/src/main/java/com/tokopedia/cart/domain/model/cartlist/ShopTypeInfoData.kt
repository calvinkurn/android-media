package com.tokopedia.cart.domain.model.cartlist

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopTypeInfoData(
        var shopTier: Int = 0,
        var shopGrade: Int = 0,
        var shopBadge: String = "",
        var badgeSvg: String = "",
        var title: String = "",
        var titleFmt: String = ""
) : Parcelable