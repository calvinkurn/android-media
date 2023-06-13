package com.tokopedia.purchase_platform.common.feature.gifting.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnButtonModel(
    var leftIconUrl: String = "",
    var rightIconUrl: String = "",
    var description: String = "",
    var action: Int = 0,
    var title: String = ""
) : Parcelable
