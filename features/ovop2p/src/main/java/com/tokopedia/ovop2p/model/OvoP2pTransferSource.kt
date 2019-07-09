package com.tokopedia.ovop2p.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class OvoP2pTransferSource : Serializable{
    @SerializedName("name")
    var name: String = ""
    @SerializedName("phone")
    var phone: String = ""
}
