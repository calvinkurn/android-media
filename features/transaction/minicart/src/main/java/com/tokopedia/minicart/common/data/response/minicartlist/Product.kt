package com.tokopedia.minicart.common.data.response.minicartlist

import com.google.gson.annotations.SerializedName

data class Product(
        @SerializedName("product_id")
        val productId: String = "",
        @SerializedName("product_weight")
        val productWeight: Int = 0,
        @SerializedName("product_quantity")
        val productQuantity: Int = 0,
        @SerializedName("product_name")
        val productName: String = "",
        @SerializedName("product_image")
        val productImage: ProductImage = ProductImage(),
        @SerializedName("variant_description_detail")
        val variantDescriptionDetail: VariantDescriptionDetail = VariantDescriptionDetail(),
        @SerializedName("product_warning_message")
        val productWarningMessage: String = "",
        @SerializedName("slash_price_label")
        val slashPriceLabel: String = "",
        @SerializedName("product_original_price")
        val productOriginalPrice: Long = 0,
        @SerializedName("initial_price")
        val initialPrice: Long = 0L,
        @SerializedName("product_price")
        val productPrice: Long = 0,
        @SerializedName("product_information")
        val productInformation: List<String> = emptyList(),
        @SerializedName("product_notes")
        val productNotes: String = "",
        @SerializedName("product_min_order")
        val productMinOrder: Int = 0,
        @SerializedName("product_max_order")
        val productMaxOrder: Int = 0,
        @SerializedName("parent_id")
        val parentId: String = "",
        @SerializedName("wholesale_price")
        val wholesalePrice: List<WholesalePrice> = emptyList()
)