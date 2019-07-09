package com.tokopedia.ovop2p.model

import com.google.gson.annotations.SerializedName

class OvoP2pTransferConfirm {

    @SerializedName("status")
    lateinit var status: String

    @SerializedName("transfer_id")
    lateinit var transferId: String

    @SerializedName("transaction_id")
    lateinit var transactionId: String

    @SerializedName("pin_url")
    lateinit var pinUrl: String

    @SerializedName("receiver_link")
    var rcvrLink: Boolean = false
    
    @SerializedName("errors")
    var errors: List<Map<String, String>>? = null
}
