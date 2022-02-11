package com.tokopedia.sellerorder.detail.data.model


import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddOnSummary(
    @SerializedName("addons")
    @Expose
    val addons: List<Addon> = listOf(),
    @SerializedName("total")
    @Expose
    val total: Int = 0,
    @SerializedName("total_price")
    @Expose
    val totalPrice: String = "",
    @SerializedName("total_price_str")
    @Expose
    val totalPriceStr: String = "",
    @SerializedName("total_quantity")
    @Expose
    val totalQuantity: Int = 0
) {
    data class Addon(
        @SerializedName("create_time")
        @Expose
        val createTime: String = "",
        @SerializedName("id")
        @Expose
        val id: String = "0",
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",
        @SerializedName("level")
        @Expose
        val level: Int = 0,
        @SerializedName("metadata")
        @Expose
        val metadata: Metadata? = null,
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("order_id")
        @Expose
        val orderId: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        @Expose
        val price: String = "",
        @SerializedName("price_str")
        @Expose
        val priceStr: String = "",
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0,
        @SerializedName("reference_id")
        @Expose
        val referenceId: String = "",
        @SerializedName("subtotal_price")
        @Expose
        val subtotalPrice: String = "",
        @SerializedName("subtotal_price_str")
        @Expose
        val subtotalPriceStr: String = "",
        @SerializedName("type")
        @Expose
        val type: String = ""
    ) {
        data class Metadata(
            @SerializedName("from")
            @Expose
            val from: String = "",
            @SerializedName("notes")
            @Expose
            val notes: String = "",
            @SerializedName("to")
            @Expose
            val to: String = ""
        ) {
            fun isEmpty(): Boolean = from.isBlank() && to.isBlank() && notes.isBlank()
        }
    }
}