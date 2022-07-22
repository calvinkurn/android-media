package com.tokopedia.tokofood.common.domain.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.param.KeroAddressParamData

data class KeroGetAddressResponse(
    @SerializedName("kero_get_address")
    val keroGetAddress: KeroGetAddress = KeroGetAddress()
)

data class KeroGetAddress(
    @SerializedName("data")
    val data: List<KeroAddressParamData> = listOf()
)