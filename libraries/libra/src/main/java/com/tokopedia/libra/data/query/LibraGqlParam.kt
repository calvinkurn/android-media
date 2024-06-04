package com.tokopedia.libra.data.query

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class LibraGqlParam(
    @SerializedName("type") val type: String
) : GqlParam
