package com.tokopedia.topads.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class InsightProductRecommendationModel(
        var id: String = "",
        var name: String = "",
        var searchNumber: Int = 0,
        var searchPercent: String = "",
        var recommendedBid: String = "",
        var pricebid: String = ""

) : Parcelable