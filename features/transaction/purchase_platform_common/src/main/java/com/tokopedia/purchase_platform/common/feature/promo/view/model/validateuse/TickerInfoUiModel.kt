package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TickerInfoUiModel(
    var uniqueId: String = "",
    var statusCode: Int = -1,
    var message: String = ""
) : Parcelable
