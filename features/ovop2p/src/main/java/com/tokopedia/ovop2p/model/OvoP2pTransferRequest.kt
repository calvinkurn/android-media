package com.tokopedia.ovop2p.model

import com.google.gson.annotations.SerializedName

class OvoP2pTransferRequest {
    @SerializedName("errors")
    var errors: List<Map<String, String>>? = null

    @SerializedName("dest_acc_name")
    lateinit var dstAccName: String
}
