package com.tokopedia.loginregister.ticker.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by ade on 8/5/2019
 */
class TickerInfoData(
    @SerializedName("ticker") @Expose var tickersInfoPojo: TickersInfoPojo
)