package com.tokopedia.loginregister.common.domain.pojo

import com.google.gson.annotations.SerializedName

class TickerInfoData(
    @SerializedName("ticker") var tickersInfoPojo: TickersInfoPojo
)

class TickersInfoPojo(
    @SerializedName("tickers") var listTickerInfoPojo: List<TickerInfoPojo>
)

class TickerInfoPojo(
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("color") val color: String
)
