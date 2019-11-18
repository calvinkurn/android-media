package com.tokopedia.v2.home.model.pojo.wallet

import com.google.gson.annotations.SerializedName

data class WalletData(
        @SerializedName("wallet")
        val wallet: Wallet = Wallet(),
        @SerializedName("tokopointsDrawer")
        val tokopoint: Tokopoint = Tokopoint()
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WalletData

        if (wallet != other.wallet) return false
        if (tokopoint != other.tokopoint) return false

        return true
    }

    override fun hashCode(): Int {
        var result = wallet.hashCode()
        result = 31 * result + tokopoint.hashCode()
        return result
    }
}