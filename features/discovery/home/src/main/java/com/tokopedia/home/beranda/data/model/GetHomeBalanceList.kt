package com.tokopedia.home.beranda.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by dhaba
 */
data class GetHomeBalanceList(
    @Expose
    @SerializedName("balances")
    val balancesList: List<GetHomeBalanceItem> = mutableListOf(),
    @Expose
    @SerializedName("error")
    val error: String = ""
)