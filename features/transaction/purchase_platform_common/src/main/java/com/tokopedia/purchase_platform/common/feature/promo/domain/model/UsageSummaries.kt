package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 16/03/20.
 */
data class UsageSummaries(
        @field:SerializedName("description")
        val desc: String? = null,

        @field:SerializedName("type")
        val type: String? = null,

        @field:SerializedName("amount_str")
        val amountStr: String? = null,

        @field:SerializedName("amount")
        val amount: Int? = null,

        @field:SerializedName("currency_details_str")
        val currencyDetailsStr: String = ""
)