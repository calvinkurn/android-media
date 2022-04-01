package com.tokopedia.orderhistory.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class OrderHistoryParam(
    @SerializedName("shopID")
    val ShopID: String,

    @SerializedName("minOrderTime")
    val paramMinOrderTime: String
): GqlParam
