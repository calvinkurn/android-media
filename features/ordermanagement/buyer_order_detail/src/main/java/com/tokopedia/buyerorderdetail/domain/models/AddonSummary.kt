package com.tokopedia.buyerorderdetail.domain.models

import com.google.gson.annotations.SerializedName


data class AddonSummary(
    @SerializedName("addons")
    val addons: List<Addon>? = listOf(),
    @SerializedName("total")
    val total: Int = 0,
    @SerializedName("total_price")
    val totalPrice: Double = 0.0,
    @SerializedName("total_price_str")
    val totalPriceStr: String = "",
    @SerializedName("total_quantity")
    val totalQuantity: Int = 0
) {
    data class Addon(
        @SerializedName("id")
        val id: String = "0",
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("level")
        val level: Int = 0,
        @SerializedName("metadata")
        val metadata: Metadata? = Metadata(),
        @SerializedName("name")
        val name: String = "",
        @SerializedName("order_id")
        val orderId: String = "0",
        @SerializedName("price")
        val price: Double = 0.0,
        @SerializedName("price_str")
        val priceStr: String = "",
        @SerializedName("quantity")
        val quantity: Int = 0,
        @SerializedName("reference_id")
        val referenceId: Long = 0,
        @SerializedName("subtotal_price")
        val subtotalPrice: Double = 0.0,
        @SerializedName("subtotal_price_str")
        val subtotalPriceStr: String = "",
        @SerializedName("type")
        val type: String = ""
    ) {
        data class Metadata(
            @SerializedName("add_on_note")
            val addOnNote: AddOnNote = AddOnNote()
        ) {
            data class AddOnNote(
                @SerializedName("from")
                val from: String = "",
                @SerializedName("is_custom_note")
                val isCustomNote: Boolean = false,
                @SerializedName("notes")
                val notes: String = "",
                @SerializedName("short_notes")
                val shortNotes: String = "",
                @SerializedName("to")
                val to: String = ""
            )
        }
    }
}
