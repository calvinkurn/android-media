package com.tokopedia.topads.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class InsightProductRecommendationModel(
        var productid: String = "",
        var productname: String = "",
        var searchCount: Int = 0,
        var serachPercentage: String = "",
        var recommendedBid: String = "",
        var pricebid: String = ""

) : Parcelable