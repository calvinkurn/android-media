package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PopUpData(
        var button: ButtonData = ButtonData(),
        var description: String = "",
        var title: String = ""
) : Parcelable
