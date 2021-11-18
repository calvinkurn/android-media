package com.tokopedia.checkout.old.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PreorderData(
        var isPreorder: Boolean = false,
        var duration: String = ""
) : Parcelable