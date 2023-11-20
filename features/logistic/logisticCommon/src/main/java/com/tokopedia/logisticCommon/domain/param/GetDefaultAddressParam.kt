package com.tokopedia.logisticCommon.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetDefaultAddressParam(
    @SerializedName("source")
    val source: String = "",

    @SerializedName("track_activity")
    val needToTrack: Boolean = false
) : GqlParam
