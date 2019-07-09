package com.tokopedia.ovop2p.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OvoP2pTransferThankyou : Serializable{
    @SerializedName("transfer_id")
    var trnsfrId: Int = 0
    @SerializedName("transaction_id")
    var txnId: Int = 0
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
    @SerializedName("message")
    var msg: String = ""
    @SerializedName("reference_number")
    var refNum: Int = 0
    @SerializedName("errors")
    var errors: List<Map<String, String>>? = null
}
