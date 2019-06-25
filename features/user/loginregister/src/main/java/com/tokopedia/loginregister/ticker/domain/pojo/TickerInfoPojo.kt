package com.tokopedia.loginregister.ticker.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by ade on 8/5/2019
 */
class TickerInfoPojo(
    @SerializedName("id") @Expose val id: Int,
    @SerializedName("title") @Expose val title: String,
    @SerializedName("message") @Expose val message: String,
    @SerializedName("ticker_type") @Expose val tickerType: Int,
    @SerializedName("color") @Expose val color: String
)
