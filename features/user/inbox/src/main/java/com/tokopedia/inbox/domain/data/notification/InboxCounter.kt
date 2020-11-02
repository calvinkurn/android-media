package com.tokopedia.inbox.domain.data.notification


import com.google.gson.annotations.SerializedName

data class InboxCounter(
    @SerializedName("all")
    val all: All = All(),
    @SerializedName("buyer")
    val buyer: Buyer = Buyer(),
    @SerializedName("seller")
    val seller: Seller = Seller()
)