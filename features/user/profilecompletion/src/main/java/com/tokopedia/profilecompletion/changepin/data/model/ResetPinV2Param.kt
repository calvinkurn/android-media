package com.tokopedia.profilecompletion.changepin.data.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class ResetPinV2Param(
    @SerializedName("pin_token")
    val pinToken: String = "",
    @SerializedName("validate_token")
    val validateToken: String = ""
) : GqlParam
