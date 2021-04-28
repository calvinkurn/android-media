package com.tokopedia.topads.common.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class InsightDailyBudgetModel(
        var groupId: String = "",
        var groupName: String = "",
        var dailySuggestedPrice: Double = 0.0,
        var potentialClick: Int = 0,
        var priceDaily: Double = 0.0
) :Parcelable