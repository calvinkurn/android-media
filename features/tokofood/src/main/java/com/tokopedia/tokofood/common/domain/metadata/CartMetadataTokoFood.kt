package com.tokopedia.tokofood.common.domain.metadata

import android.annotation.SuppressLint
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CartMetadataTokoFood(
    @SerializedName("variants")
    @Expose
    val variants: List<CartMetadataVariantTokoFood> = listOf(),
    @SerializedName("notes")
    @Expose
    val notes: String = "",
    @SerializedName("item_price")
    @Expose
    val itemPrice: Long = 0,
    @SerializedName("total_price")
    @Expose
    val totalPrice: Long = 0
) {

    fun generateString(): String = Gson().toJson(this)

}

// TODO: Check for more available attributes
data class CartMetadataVariantTokoFood(
    @SerializedName("variant_id")
    @Expose
    val variantId: String = "",
    @SerializedName("display_name")
    @Expose
    val displayName: String = "",
    @SerializedName("option_id")
    @Expose
    val optionId: String = "",
    @SerializedName("option_name")
    @Expose
    val optionName: String = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    @Expose
    val price: Long = 0
)