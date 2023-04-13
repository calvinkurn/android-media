package com.tokopedia.logisticseller.data.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class NewDriverParam(
    @SerializedName("order_id")
    val orderId: String = "",
) : GqlParam
