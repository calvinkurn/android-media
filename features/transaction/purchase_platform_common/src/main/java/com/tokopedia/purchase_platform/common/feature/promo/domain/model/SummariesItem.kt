package com.tokopedia.purchase_platform.common.feature.promo.domain.model

import com.google.gson.annotations.SerializedName

data class SummariesItem(
        @field:SerializedName("amount")
        val amount: Int = 0,
        @field:SerializedName("section_name")
        val sectionName: String = "",
        @field:SerializedName("description")
        val description: String = "",
        @field:SerializedName("details")
        val details: List<DetailsItem> = emptyList(),
        @field:SerializedName("section_description")
        val sectionDescription: String = "",
        @field:SerializedName("type")
        val type: String = "",
        @field:SerializedName("amount_str")
        val amountStr: String = ""
)