package com.tokopedia.localizationchooseaddress.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetChosenAddressParam(
    @SerializedName("source")
    private val source: String = "",
    @SerializedName("is_tokonow_request")
    private val isTokonow: Boolean = true
) : GqlParam
