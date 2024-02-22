package com.tokopedia.sellerorder.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddOnSummary(
    @SerializedName("label")
    val label: String = "",
    @SerializedName("addons")
    @Expose
    val addons: List<Addon> = listOf(),
    @SerializedName("total_price_str")
    @Expose
    val totalPriceStr: String = ""
) {
    data class Addon(
        @SerializedName("id")
        @Expose
        val id: String = "0",
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",
        @SerializedName("metadata")
        @Expose
        val metadata: Metadata? = null,
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("price_str")
        @Expose
        val priceStr: String = "",
        @SerializedName("quantity")
        @Expose
        val quantity: Int = 0,
        @SerializedName("type")
        @Expose
        val type: String = ""
    ) {
        data class Metadata(
            @SerializedName("add_on_note")
            @Expose
            val addOnNote: AddOnNote? = null,
            @SerializedName("info_link")
            @Expose
            val infoLink: String = "",
        ) {
            data class AddOnNote(
                @SerializedName("from")
                @Expose
                val from: String = "",
                @SerializedName("notes")
                @Expose
                val notes: String = "",
                @SerializedName("to")
                @Expose
                val to: String = "",
                @SerializedName("tips")
                @Expose
                val tips: String = ""
            ) {
                fun isEmpty(): Boolean =
                    from.isBlank() && to.isBlank() && notes.isBlank() && tips.isBlank()
            }
        }
    }
}
