package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class Data(
        @SerializedName("available_section")
        val availableSection: AvailableSection = AvailableSection(),
        @SerializedName("empty_cart")
        val emptyCart: EmptyCart = EmptyCart(),
        @SerializedName("errors")
        val errors: List<String> = emptyList(),
        @SerializedName("hashed_email")
        val hashedEmail: String = "",
        @SerializedName("header_title")
        val headerTitle: String = "",
        @SerializedName("max_char_note")
        val maxCharNote: Int = 0,
        @SerializedName("max_quantity")
        val maxQuantity: Int = 0,
        @SerializedName("out_of_service")
        val outOfService: OutOfService = OutOfService(),
        @SerializedName("shopping_summary")
        val shoppingSummary: ShoppingSummary = ShoppingSummary(),
        @SerializedName("tickers")
        val tickers: List<Ticker> = emptyList(),
        @SerializedName("total_product_count")
        val totalProductCount: Int = 0,
        @SerializedName("total_product_error")
        val totalProductError: Int = 0,
        @SerializedName("total_product_price")
        val totalProductPrice: Long = 0L,
        @SerializedName("unavailable_section")
        val unavailableSection: List<UnavailableSection> = emptyList(),
        @SerializedName("unavailable_section_action")
        val unavailableSectionAction: List<UnavailableSectionAction> = emptyList(),
        @SerializedName("unavailable_ticker")
        val unavailableTicker: String = ""
)