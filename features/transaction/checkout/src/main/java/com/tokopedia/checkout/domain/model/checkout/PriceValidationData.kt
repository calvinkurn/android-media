package com.tokopedia.checkout.domain.model.checkout

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PriceValidationData(
        var isUpdated: Boolean = false,
        var message: MessageData = MessageData(),
        var trackerData: TrackerData = TrackerData()
) : Parcelable