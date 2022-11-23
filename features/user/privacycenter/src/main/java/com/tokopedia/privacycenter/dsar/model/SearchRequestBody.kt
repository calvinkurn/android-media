package com.tokopedia.privacycenter.dsar.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class SearchRequestBody(
    @SerializedName("term")
    var email: String = "",
): GqlParam
