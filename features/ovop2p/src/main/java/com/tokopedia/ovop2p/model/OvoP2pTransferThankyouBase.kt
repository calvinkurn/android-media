package com.tokopedia.ovop2p.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OvoP2pTransferThankyouBase : Serializable{
    @SerializedName("goalP2PThanks")
    lateinit var ovoP2pTransferThankyou: OvoP2pTransferThankyou
}
