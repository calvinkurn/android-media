package com.tokopedia.dilayanitokopedia.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetRecomForYouDTParam(
    @SerializedName("page")
    val page: String = "",
    @SerializedName("location")
    val location: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("productPage")
    val productPage: Int = 0
) : GqlParam
