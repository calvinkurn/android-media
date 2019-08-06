package com.tokopedia.ovop2p.model

import com.google.gson.annotations.SerializedName

data class OvoP2pTransferRequest(
        @SerializedName("errors")
        var errors: List<Map<String, String>>? = null,

        @SerializedName("dest_acc_name")
        var dstAccName: String? = ""
)
