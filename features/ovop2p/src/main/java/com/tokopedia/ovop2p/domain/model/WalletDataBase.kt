package com.tokopedia.ovop2p.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WalletDataBase (
    @SerializedName("wallet")
    @Expose
    val wallet: Wallet? = null
)