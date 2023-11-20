package com.tokopedia.logisticCommon.domain.param

import com.google.gson.annotations.SerializedName
import com.tokopedia.graphql.data.GqlParam

data class GetDetailAddressParam(
    @SerializedName("addr_ids")
    val addressIds: String = "",

    @SerializedName("extract_address_detail")
    val isManageAddressFlow: Boolean = true,

    @SerializedName("source")
    val source: String = "",

    @SerializedName("track_activity")
    val needToTrack: Boolean = false
) : GqlParam

data class KeroGetAddressInput(
    @SerializedName("input")
    val input: GetDetailAddressParam = GetDetailAddressParam()
) : GqlParam
