package com.tokopedia.ovop2p.model

import com.google.gson.annotations.SerializedName

data class OvoP2pTransferConfirm(

        @SerializedName("status")
        var status: String? = "",

        @SerializedName("transfer_id")
        var transferId: String? = "",

        @SerializedName("transaction_id")
        var transactionId: String? = "",

        @SerializedName("pin_url")
        var pinUrl: String? = "",

        @SerializedName("receiver_link")
        var rcvrLink: Boolean = false,

        @SerializedName("errors")
        var errors: List<Map<String, String>>?
)
