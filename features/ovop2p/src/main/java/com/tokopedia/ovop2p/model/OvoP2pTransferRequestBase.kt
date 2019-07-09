package com.tokopedia.ovop2p.model

import com.google.gson.annotations.SerializedName

class OvoP2pTransferRequestBase {

    @SerializedName("goalP2PRequest")
    lateinit var ovoP2pTransferRequest: OvoP2pTransferRequest
}
