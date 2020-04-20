package com.tokopedia.purchase_platform.common.data.model.response.validateuse

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 16/03/20.
 */
data class UsageSummaries (
        @field:SerializedName("description")
        val description: String = "",

        @field:SerializedName("type")
        val type: String = "",

        @field:SerializedName("amount_str")
        val amountString: String = "",

        @field:SerializedName("amount")
        val amount: Int = -1)