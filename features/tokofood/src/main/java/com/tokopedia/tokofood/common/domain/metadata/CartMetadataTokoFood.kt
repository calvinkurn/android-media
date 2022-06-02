package com.tokopedia.tokofood.common.domain.metadata

import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CartMetadataTokoFood(
    @SerializedName("notes")
    @Expose
    val notes: String = "",
) {

    fun generateString(): String = Gson().toJson(this)

}

data class CartMetadataTokoFoodWithVariant(
    @SerializedName("variants")
    @Expose
    val variants: List<CartMetadataVariantTokoFood> = listOf(),
    @SerializedName("notes")
    @Expose
    val notes: String = "",
) {

    fun generateString(): String = Gson().toJson(this)

}

data class CartMetadataVariantTokoFood(
    @SerializedName("variant_id")
    @Expose
    val variantId: String = "",
    @SerializedName("option_id")
    @Expose
    val optionId: String = ""
)