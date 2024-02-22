package com.tokopedia.buyerorderdetail.domain.models

import com.google.gson.annotations.SerializedName


data class AddonSummary(
    @SerializedName("addons")
    val addons: List<Addon>? = listOf(),
    @SerializedName("total_price_str")
    val totalPriceStr: String = ""
) {
    data class Addon(
        @SerializedName("id")
        val id: String = "0",
        @SerializedName("image_url")
        val imageUrl: String = "",
        @SerializedName("metadata")
        val metadata: Metadata? = Metadata(),
        @SerializedName("name")
        val name: String = "",
        @SerializedName("price_str")
        val priceStr: String = "",
        @SerializedName("quantity")
        val quantity: Int = 0,
        @SerializedName("type")
        val type: String = ""
    ) {
        data class Metadata(
            @SerializedName("add_on_note")
            val addOnNote: AddOnNote = AddOnNote(),
            @SerializedName("info_link")
            val infoLink: String = ""
        ) {
            data class AddOnNote(
                @SerializedName("from")
                val from: String = "",
                @SerializedName("notes")
                val notes: String = "",
                //Render as HTML and no need to truncate with "selengkapnya"
                @SerializedName("tips")
                val tips: String = "",
                @SerializedName("to")
                val to: String = ""
            )
        }
    }
}
