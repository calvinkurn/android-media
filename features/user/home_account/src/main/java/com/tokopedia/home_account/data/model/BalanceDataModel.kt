package com.tokopedia.home_account.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Ade Fulki on 22/02/21.
 */

data class BalanceDataModel(
        @SerializedName("balance") @Expose
        val data: Balance = Balance()
)

data class Balance(
        @SerializedName("buyer_all") @Expose
        val buyerAll: Float = 0f,
        @SerializedName("buyer_all_fmt") @Expose
        val buyerAllFmt: String = "",
        @SerializedName("buyer_hold") @Expose
        val buyerHold: Float = 0f,
        @SerializedName("buyer_hold_fmt") @Expose
        val buyerHoldFmt: String = "",
        @SerializedName("buyer_usable") @Expose
        val buyerUsable: Float = 0f,
        @SerializedName("buyer_usable_fmt") @Expose
        val buyerUsable_fmt: String = "",
        @SerializedName("seller_all") @Expose
        val sellerAll: Float = 0f,
        @SerializedName("seller_all_fmt") @Expose
        val sellerAll_fmt: String = "",
        @SerializedName("seller_hold") @Expose
        val sellerHold: Float = 0f,
        @SerializedName("seller_hold_fmt") @Expose
        val sellerHoldFmt: String = "",
        @SerializedName("seller_usable") @Expose
        val sellerUsable: Float = 0f,
        @SerializedName("seller_usable_fmt") @Expose
        val sellerUsable_fmt: String = "",
        @SerializedName("have_error") @Expose
        val haveError: Boolean = true,
        @SerializedName("is_whitelist") @Expose
        val isWhitelist: Boolean = false
)