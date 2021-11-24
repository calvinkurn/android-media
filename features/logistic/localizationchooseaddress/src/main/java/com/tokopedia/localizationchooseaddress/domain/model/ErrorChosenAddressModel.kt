package com.tokopedia.localizationchooseaddress.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorChosenAddressModel(
    var code: Int = 0,
    var title: String = ""
): Parcelable