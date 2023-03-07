package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Donation(
    var title: String = "",
    var nominal: Int = 0,
    var description: String = "",
    var isChecked: Boolean = false
) : Parcelable
