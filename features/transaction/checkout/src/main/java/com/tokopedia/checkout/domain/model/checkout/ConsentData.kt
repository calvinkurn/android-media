package com.tokopedia.checkout.domain.model.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConsentData(
    var show: Boolean = false,
    var title: String = "",
    var text: String = ""
) : Parcelable
