package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by dhaba
 */
data class GetHomeBalanceWidgetData(
    @Expose
    @SerializedName("getHomeBalanceWidget")
    val getHomeBalanceList: GetHomeBalanceList = GetHomeBalanceList()
)