package com.tokopedia.product.addedit.detail.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetDefaultCommissionRulesRequest (
    @Expose
    @SerializedName("category_ids")
    var categoryIds: List<Int> = listOf()
)
