package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MessageUiModel(
    var color: String = "",
    var state: String = "",
    var text: String = ""
) : Parcelable
