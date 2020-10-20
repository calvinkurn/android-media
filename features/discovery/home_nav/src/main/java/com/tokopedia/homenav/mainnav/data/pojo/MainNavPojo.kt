package com.tokopedia.homenav.mainnav.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.common_wallet.balance.data.entity.WalletBalanceResponse
import com.tokopedia.common_wallet.balance.view.WalletBalanceModel

data class MainNavPojo(
        @SerializedName("id")
        @Expose
        val id: String = "",
        @SerializedName("wallet")
        @Expose
        var wallet: WalletBalanceModel = WalletBalanceModel()
)