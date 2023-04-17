package com.tokopedia.sessioncommon.data.ocl

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetOclStatusParam(
    @SerializedName("ocl_jwt_token")
    var token: String = ""
): GqlParam
