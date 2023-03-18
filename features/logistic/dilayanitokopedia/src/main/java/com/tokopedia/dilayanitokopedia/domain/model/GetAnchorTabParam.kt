package com.tokopedia.dilayanitokopedia.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetAnchorTabParam(
    @SerializedName("param")
    val param: String = "",
    @SerializedName("location")
    val location: String = ""
) : GqlParam
