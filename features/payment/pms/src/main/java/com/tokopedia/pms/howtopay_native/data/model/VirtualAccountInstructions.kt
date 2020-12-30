package com.tokopedia.pms.howtopay_native.data.model

import com.google.gson.annotations.SerializedName

data class VirtualAccountInstructions(
    @SerializedName("gateway_code")
    val gatewayCode: String,

    @SerializedName("gateway_name")
    val gatewayName: String,

    @SerializedName("bank_code")
    val bankCode: String? = null,

    @SerializedName("instructions")
    val virtualAccountChannel: ArrayList<VirtualAccountChannel>? = null
)


class VirtualAccountChannel (
    @SerializedName("payment_channel")
    val channelTitle : String,
    @SerializedName("channel_steps")
    val channelSteps : ArrayList<String>,
    var isExpanded : Boolean
)



