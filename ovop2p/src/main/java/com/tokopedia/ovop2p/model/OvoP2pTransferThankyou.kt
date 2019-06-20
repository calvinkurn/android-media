package com.tokopedia.ovop2p.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OvoP2pTransferThankyou : Serializable{
    @SerializedName("transfer_id")
    var trnsfrId: String? = ""
    @SerializedName("transaction_id")
    lateinit var txnId: String
    @SerializedName("status")
    var status: String = ""
    @SerializedName("amount")
    var amt: Int = 0
    @SerializedName("source_of_fund")
    var srcFnd: String = ""
    @SerializedName("transfer_date")
    var trnsfrDate: String = ""
    @SerializedName("source")
    lateinit var source: OvoP2pTransferSource
    @SerializedName("source")
    lateinit var soure1: OvoP2pTransferSource
    @SerializedName("message")
    var msg: String = ""
    @SerializedName("reference_number")
    var refNum: String = ""
    @SerializedName("errors")
    lateinit var errors: Errors
}
