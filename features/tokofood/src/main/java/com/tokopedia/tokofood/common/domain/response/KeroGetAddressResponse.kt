package com.tokopedia.tokofood.purchase.purchasepage.domain.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.tokofood.common.domain.param.KeroAddressParamData

data class KeroGetAddressResponse(
    @SerializedName("kero_get_address")
    @Expose
    val keroGetAddress: KeroGetAddress = KeroGetAddress()
)

data class KeroGetAddress(
    @SerializedName("data")
    @Expose
    val data: List<KeroAddressParamData> = listOf()
)