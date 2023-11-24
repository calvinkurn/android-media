package com.tokopedia.localizationchooseaddress.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetDefaultChosenAddressParam(
    @SerializedName("source")
    private val source: String = "",
    @SerializedName("lat_long")
    private val latLong: String? = null,
    @SerializedName("is_tokonow_request")
    private val isTokonow: Boolean = true
) : GqlParam
