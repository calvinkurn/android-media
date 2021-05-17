package com.tokopedia.minicart.common.data.response.minicartlistsimplified

import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("total_product_count")
        val totalProductCount: Int = 0,
        @SerializedName("total_product_error")
        val totalProductError: Int = 0,
        @SerializedName("total_product_price")
        val totalProductPrice: Long = 0,
        @SerializedName("available_section")
        val availableSection: AvailableSection = AvailableSection(),
        @SerializedName("unavailable_section")
        val unavailableSection: List<UnavailableSection> = emptyList()
)