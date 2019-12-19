package com.tokopedia.topchat.chatlist.pojo.chatblastseller


import com.google.gson.annotations.SerializedName

data class QuotaGroup(
    @SerializedName("quotaActualBuyers")
    val quotaActualBuyers: Int = 0,
    @SerializedName("quotaFollowers")
    val quotaFollowers: Int = 0,
    @SerializedName("quotaPotentialBuyers")
    val quotaPotentialBuyers: Int = 0
)