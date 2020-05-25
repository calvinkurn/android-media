package com.tokopedia.product.addedit.preview.data.source.api.param

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetProductV3Param(
        @SerializedName("productID")
        @Expose
        val productID: String,
        @SerializedName("options")
        @Expose
        val options: OptionV3
)

data class OptionV3(
        @SerializedName("edit")
        @Expose
        val edit: Boolean = true,
        @SerializedName("category")
        @Expose
        val category: Boolean = true,
        @SerializedName("catalog")
        @Expose
        val catalog: Boolean = true,
        @SerializedName("wholesale")
        @Expose
        val wholesale: Boolean = true,
        @SerializedName("preorder")
        @Expose
        val preorder: Boolean = true,
        @SerializedName("picture")
        @Expose
        val picture: Boolean = true,
        @SerializedName("sku")
        @Expose
        val sku: Boolean = true,
        @SerializedName("video")
        @Expose
        val video: Boolean = true,
        @SerializedName("variant")
        @Expose
        val variant: Boolean = true
)