package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnButton(
    var leftIconUrl: String = "",
    var rightIconUrl: String = "",
    var description: String = "",
    var action: Int = 0,
    var title: String = ""
) : Parcelable
