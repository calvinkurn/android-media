package com.tokopedia.chooseaccount.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class DeleteOclParam (
    @SerializedName("ocl_jwt_token")
    val token: String = "",
    @SerializedName("ocl_token")
    val userToken: String = ""
): GqlParam
