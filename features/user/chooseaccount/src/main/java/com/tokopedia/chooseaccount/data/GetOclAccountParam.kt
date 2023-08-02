package com.tokopedia.chooseaccount.data

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetOclAccountParam (
    @SerializedName("ocl_jwt_token")
    val token: String = ""
): GqlParam
