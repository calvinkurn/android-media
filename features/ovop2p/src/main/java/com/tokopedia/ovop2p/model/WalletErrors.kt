package com.tokopedia.ovop2p.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletErrors (
    @SerializedName("message")
    @Expose
    val message: String = "",
    @SerializedName("name")
    @Expose
    val name: String = ""
)

