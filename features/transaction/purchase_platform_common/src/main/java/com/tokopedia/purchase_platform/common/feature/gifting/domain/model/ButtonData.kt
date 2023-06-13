package com.tokopedia.purchase_platform.common.feature.gifting.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ButtonData(
    var text: String = ""
) : Parcelable
