package com.tokopedia.topads.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class InsightDailyBudgetModel(
        var id: String = "",
        var name: String = "",
        var dailySuggestedPrice: Double = 0.0,
        var potentialClick: Long = 0,
        var priceDaily: Double = 0.0
) :Parcelable