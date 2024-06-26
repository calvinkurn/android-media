package com.tokopedia.topads.dashboard.data.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.topads.common.data.model.ErrorsItem
import kotlinx.android.parcel.Parcelize

data class DailyBudgetRecommendationModel(
        @SerializedName("topadsGetDailyBudgetRecommendationV2")
        val topadsGetDailyBudgetRecommendation: TopadsGetDailyBudgetRecommendationV2 = TopadsGetDailyBudgetRecommendationV2()
)

@Parcelize
data class TopadsGetDailyBudgetRecommendationV2(
        @SerializedName("data")
        val data: List<DataBudget> = listOf(),
        @SerializedName("errors")
        val errors: List<ErrorsItem> = listOf()
):Parcelable

@Parcelize
data class DataBudget(
        @SerializedName("avg_bid")
        val avgBid: String = "0",
        @SerializedName("group_id")
        val groupId: String = "0",
        @SerializedName("group_name")
        val groupName: String = "",
        @SerializedName("price_daily")
        val priceDaily: Double = 0.0,
        @SerializedName("suggested_price_daily")
        val suggestedPriceDaily: Double = 0.0,
        var setCurrentBid: Double = 0.0,
        var setPotensiKlik: Long = 0,
        val impressHolder: ImpressHolder = ImpressHolder()
):Parcelable
