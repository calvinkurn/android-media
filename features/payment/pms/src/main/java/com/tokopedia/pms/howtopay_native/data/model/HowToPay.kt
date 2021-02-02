package com.tokopedia.pms.howtopay_native.data.model

import com.google.gson.annotations.SerializedName

data class HowToPay(
        @SerializedName("VA")
        val virtualAccountGatewayList: ArrayList<MultiChannelGateway>,
        @SerializedName("SYARIAH")
        val syariahGatewayList: ArrayList<MultiChannelGateway>,
        @SerializedName("TRANSFER")
        val bankTransferInstructions: ArrayList<String>,
        @SerializedName("STORE")
        val storeInstructions: ArrayList<String>,
        @SerializedName("KLIKBCA")
        val klikBCA: ArrayList<String>
)

data class MultiChannelGateway(
        @SerializedName("gateway_code")
        val gatewayCode: String,
        @SerializedName("gateway_name")
        val gatewayName: String,
        @SerializedName("bank_code")
        val bankCode: String? = null,
        @SerializedName("channel")
        val paymentChannels: ArrayList<PaymentChannel>? = null
)

data class PaymentChannel(
        @SerializedName("payment_channel")
        val channelTitle: String,
        @SerializedName("channel_steps")
        val channelSteps: ArrayList<String>,
        @SerializedName("notes")
        val channelNotes: String?,
        var isExpanded: Boolean
)
