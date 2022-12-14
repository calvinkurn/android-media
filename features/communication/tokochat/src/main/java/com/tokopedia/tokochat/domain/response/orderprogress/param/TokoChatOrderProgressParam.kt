package com.tokopedia.tokochat.domain.response.orderprogress.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class TokoChatOrderProgressParam(
    @SerializedName("orderID")
    val orderID: String,
    @SerializedName("serviceType")
    val serviceType: String,
): GqlParam
