package com.tokopedia.sessioncommon.data.pin

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class PinStatusParam(
    @SerializedName("id")
    val id: String,

    @SerializedName("type")
    val type: String
): GqlParam
