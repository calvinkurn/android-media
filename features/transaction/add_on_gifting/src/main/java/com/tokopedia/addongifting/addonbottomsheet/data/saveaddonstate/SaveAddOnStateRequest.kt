package com.tokopedia.addongifting.addonbottomsheet.data.saveaddonstate

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class SaveAddOnStateRequest(
        @SerializedName("add_ons")
        var addOns: List<AddOnRequest> = emptyList(),
        @SerializedName("source")
        var source: String = ""
)

data class AddOnRequest(
        @SerializedName("add_on_key")
        var addOnKey: String = "",
        @SerializedName("add_on_level")
        var addOnLevel: String = "",
        @SerializedName("cart_products")
        var cartProducts: List<CartProduct> = emptyList(),
        @SerializedName("add_on_data")
        var addOnData: List<AddOnDataRequest> = emptyList()
)

data class CartProduct(
        @SuppressLint("Invalid Data Type")
        @SerializedName("cart_id")
        var cartId: Long = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("product_id")
        var productId: Long = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("warehouse_id")
        var warehouseId: Long = 0,
        @SerializedName("product_name")
        var productName: String = "",
        @SerializedName("product_image_url")
        var productImageUrl: String = "",
        @SerializedName("product_parent_id")
        var productParentId: String = ""
)

data class AddOnDataRequest(
        @SuppressLint("Invalid Data Type")
        @SerializedName("add_on_id")
        var addOnId: Long = 0,
        @SerializedName("add_on_qty")
        var addOnQty: Int = 0,
        @SerializedName("add_on_metadata")
        var addOnMetadata: AddOnMetadataRequest = AddOnMetadataRequest()
)

data class AddOnMetadataRequest(
        @SerializedName("add_on_note")
        var addOnNote: AddOnNoteRequest = AddOnNoteRequest()
)

data class AddOnNoteRequest(
        @SerializedName("is_custom_note")
        var isCustomNote: Boolean = false,
        @SerializedName("to")
        var to: String = "",
        @SerializedName("from")
        var from: String = "",
        @SerializedName("notes")
        var notes: String = ""
)