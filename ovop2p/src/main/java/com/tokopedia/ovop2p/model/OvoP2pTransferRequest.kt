package com.tokopedia.ovop2p.model

import com.google.gson.annotations.SerializedName

class OvoP2pTransferRequest {
    @SerializedName("errors")
    lateinit var errors: Errors

    @SerializedName("dest_acc_name")
    lateinit var dstAccName: String
}
