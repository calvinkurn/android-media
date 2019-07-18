package com.tokopedia.ovop2p.model

import com.google.gson.annotations.SerializedName

data class OvoP2pTransferRequestBase(

        @SerializedName("goalP2PRequest")
        var ovoP2pTransferRequest: OvoP2pTransferRequest?
)
