package com.tokopedia.attachinvoice.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ParamInvoice(
    @SerializedName("msgId")
    val msgId: String,

    @SerializedName("page")
    val page: Int
) : GqlParam