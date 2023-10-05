package com.tokopedia.chatbot.chatbot2.data.owocinvoice

import com.google.gson.annotations.SerializedName

data class DynamicOwocInvoicePojo(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("invoice_cards")
    val invoiceCardList: List<InvoiceCardOwoc>? = null
) {
    data class InvoiceCardOwoc(
        @SerializedName("product_description")
        val productDescription: String? = null,
        @SerializedName("product_image_url")
        val productImageUrl: String? = null,
        @SerializedName("product_name")
        val productName: String? = null,
        @SerializedName("shop_badge_image_url")
        val shopBadgeImageUrl: String? = null,
        @SerializedName("shop_name")
        val shopName: String? = null,
    )
}



