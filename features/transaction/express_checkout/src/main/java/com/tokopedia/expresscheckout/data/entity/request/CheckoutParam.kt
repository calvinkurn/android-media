package com.tokopedia.expresscheckout.data.entity.request

import com.google.gson.annotations.SerializedName

/**
 * Created by Irfan Khoirul on 11/01/19.
 */

data class CheckoutParam(
        @SerializedName("account_name")
        var accountName: String? = null,

        @SerializedName("account_number")
        var accountNumber: String? = null,

        @SerializedName("bank_id")
        var bankId: String? = null
)