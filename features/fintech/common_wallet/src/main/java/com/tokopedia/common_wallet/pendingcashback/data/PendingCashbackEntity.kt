package com.tokopedia.common_wallet.pendingcashback.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by nabillasabbaha on 2/7/18.
 */

class PendingCashbackEntity(
        @SerializedName("balance")
        @Expose
        val balance: String = "",
        @SerializedName("balance_text")
        @Expose
        val balanceText: String = "",
        @SerializedName("cash_balance")
        @Expose
        val cashBalance: String = "",
        @SerializedName("cash_balance_text")
        @Expose
        val cashBalanceText: String = "",
        @SerializedName("point_balance")
        @Expose
        val pointBalance: String = "",
        @SerializedName("point_balance_text")
        @Expose
        val pointBalanceText: String = "",
        @SerializedName("wallet_type")
        @Expose
        val walletType: String = "",
        @SerializedName("phone_number")
        @Expose
        val phoneNumber: String = "")
