package com.tokopedia.common_wallet.balance.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 9/10/19.
 */
class WalletBalanceResponse(
        @SerializedName("wallet")
        @Expose
        val wallet: WalletBalanceEntity? = null)
