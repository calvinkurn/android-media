package com.tokopedia.shop.settings.etalase.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PowerMerchantAccessModel(
        val title: String = "",
        val desc: String = "",
        val imageUrl: String = "",
        val btnTitle: String = ""
): Parcelable