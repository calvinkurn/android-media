package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PreorderData(
    var isPreorder: Boolean = false,
    var duration: String = ""
) : Parcelable
