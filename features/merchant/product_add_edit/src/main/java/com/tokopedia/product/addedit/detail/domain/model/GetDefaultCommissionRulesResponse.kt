package com.tokopedia.product.addedit.detail.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetDefaultCommissionRulesResponse(
    @Expose
    @SerializedName("get_default_commission_rules")
    val getDefaultCommissionRules: GetDefaultCommissionRules
)

data class GetDefaultCommissionRules(
    @Expose
    @SerializedName("category_rates")
    val categoryRate: List<CategoryRate> = listOf()
)

data class CategoryRate(
    @Expose
    @SerializedName("commission_rules")
    val commissionRules: List<CommissionRule> = listOf()
)

data class CommissionRule(
    @Expose
    @SerializedName("shop_type")
    val shopType: Int,
    @Expose
    @SerializedName("commission_rate")
    val commissionRate: Double
)
