package com.tokopedia.ovop2p.domain.model

import com.google.gson.annotations.SerializedName

data class OvoP2pTransferConfirmBase(
        @SerializedName("goalP2PConfirm")
        var ovoP2pTransferConfirm: OvoP2pTransferConfirm?)
