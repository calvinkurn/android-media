package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 16/03/20.
 */
data class UsageSummaries(
    @field:SerializedName("description")
    val desc: String = "",
    @field:SerializedName("type")
    val type: String = "",
    @field:SerializedName("amount_str")
    val amountStr: String = "",
    @field:SerializedName("amount")
    val amount: Int = 0,
    @field:SerializedName("currency_details_str")
    val currencyDetailsStr: String = ""
)
