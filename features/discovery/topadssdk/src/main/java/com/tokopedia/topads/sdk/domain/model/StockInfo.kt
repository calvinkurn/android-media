package com.tokopedia.topads.sdk.domain.model


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockInfo(
    @SerializedName("sold_stock_percentage")
    val soldStockPercentage: Int = 0,
    @SerializedName("stock_colour")
    val stockColour: String = "",
    @SerializedName("stock_wording")
    val stockWording: String = ""
): Parcelable
