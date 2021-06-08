package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("empty_cart")
        val emptyCart: EmptyCart = EmptyCart(),
        @SerializedName("out_of_service")
        val outOfService: OutOfService = OutOfService(),
        @SerializedName("max_char_note")
        val maxCharNote: Int = 0,
        @SerializedName("header_title")
        val headerTitle: String = "",
        @SerializedName("shopping_summary")
        val shoppingSummary: ShoppingSummary = ShoppingSummary(),
        @SerializedName("unavailable_ticker")
        val unavailableTicker: String = "",
        @SerializedName("unavailable_section_action")
        val unavailableSectionAction: List<UnavailableSectionAction> = emptyList(),
        @SerializedName("unavailable_section")
        val unavailableSection: List<UnavailableSection> = emptyList(),
        @SerializedName("available_section")
        val availableSection: AvailableSection = AvailableSection(),
        @SerializedName("total_product_count")
        val totalProductCount: Int = 0,
        @SerializedName("total_product_price")
        val totalProductPrice: Long = 0L,
        @SerializedName("total_product_error")
        val totalProductError: Int = 0
)