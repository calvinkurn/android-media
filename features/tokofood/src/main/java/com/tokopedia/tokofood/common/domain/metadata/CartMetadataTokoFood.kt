package com.tokopedia.tokofood.common.domain.metadata

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class CartMetadataTokoFood(
    @SerializedName("notes")
    val notes: String = "",
) {

    fun generateString(): String = Gson().toJson(this)

}

data class CartMetadataTokoFoodWithVariant(
    @SerializedName("variants")
    val variants: List<CartMetadataVariantTokoFood> = listOf(),
    @SerializedName("notes")
    val notes: String = "",
) {

    fun generateString(): String = Gson().toJson(this)

}

data class CartMetadataVariantTokoFood(
    @SerializedName("variant_id")
    val variantId: String = "",
    @SerializedName("option_id")
    val optionId: String = ""
)
