package com.tokopedia.product.detail.data.model.ticker

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 15/09/21
 */
data class ProductTicker(
        @SerializedName("tickerInfo")
        @Expose
        val tickerInfo: List<TickerInfo> = listOf()
)

data class TickerInfo(
        @SerializedName("productIDs")
        @Expose
        val productIDs: List<String> = listOf(),
        @SerializedName("tickerData")
        @Expose
        val tickerDatumResponses: List<TickerDataResponse> = listOf()
)

data class TickerDataResponse(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("color")
        @Expose
        val color: String = "",
        @SerializedName("link")
        @Expose
        val link: String = "",
        @SerializedName("action")
        @Expose
        val action: String = "",
        @SerializedName("actionLink")
        @Expose
        val actionLink: String = "",
        @SerializedName("tickerType")
        @Expose
        val tickerType: Int = 0,
        @SerializedName("actionBottomSheet")
        @Expose
        val actionBottomSheet: TickerActionBs = TickerActionBs()
)

data class TickerActionBs(
        @SerializedName("title")
        @Expose
        val title: String = "",
        @SerializedName("message")
        @Expose
        val message: String = "",
        @SerializedName("reason")
        @Expose
        val reason: String = "",
        @SerializedName("buttonText")
        @Expose
        val buttonText: String = "",
        @SerializedName("buttonLink")
        @Expose
        val buttonLink: String = ""
)