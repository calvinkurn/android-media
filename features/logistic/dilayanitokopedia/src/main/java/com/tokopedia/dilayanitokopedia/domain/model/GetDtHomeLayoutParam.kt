package com.tokopedia.dilayanitokopedia.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetDtHomeLayoutParam(
    @SerializedName("page")
    val page: String = "",
    @SerializedName("location")
    val location: String = ""
) : GqlParam
