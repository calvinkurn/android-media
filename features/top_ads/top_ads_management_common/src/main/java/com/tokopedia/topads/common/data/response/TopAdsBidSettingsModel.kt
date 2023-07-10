package com.tokopedia.topads.common.data.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class TopAdsBidSettingsModel(
    var bidType: String? = null,
    var priceBid: Float? = 0.0f
): Parcelable
