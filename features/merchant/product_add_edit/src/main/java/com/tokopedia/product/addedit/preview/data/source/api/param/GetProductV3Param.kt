package com.tokopedia.product.addedit.preview.data.source.api.param

import com.google.gson.annotations.SerializedName

data class GetProductV3Param(
        @SerializedName("productID")
        val productID: String,
        @SerializedName("options")
        val options: OptionV3
)

data class OptionV3(
        @SerializedName("edit")
        val edit: Boolean = true,
        @SerializedName("category")
        val category: Boolean = true,
        @SerializedName("catalog")
        val catalog: Boolean = true,
        @SerializedName("wholesale")
        val wholesale: Boolean = true,
        @SerializedName("preorder")
        val preorder: Boolean = true,
        @SerializedName("picture")
        val picture: Boolean = true,
        @SerializedName("sku")
        val sku: Boolean = true,
        @SerializedName("video")
        val video: Boolean = true,
        @SerializedName("variant")
        val variant: Boolean = true,
        @SerializedName("campaign")
        val campaign: Boolean = true
)
