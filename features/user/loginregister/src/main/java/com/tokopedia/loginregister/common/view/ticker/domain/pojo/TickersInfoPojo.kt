package com.tokopedia.loginregister.common.view.ticker.domain.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by ade on 8/5/2019
 */
class TickersInfoPojo(
    @SerializedName("tickers") @Expose var listTickerInfoPojo: List<TickerInfoPojo>
)