package com.tokopedia.product.detail.common.data.model.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductDetailVariantResponse(
        @SerializedName("getPDPVariantInfo")
        @Expose
        val data: ProductVariant = ProductVariant()
)

data class ProductVariant(

        @SerializedName("parent_id")
        @Expose
        var parentId: Int? = null,
        @SerializedName("default_child")
        @Expose
        var defaultChild: Int? = null,
        @SerializedName("variant")
        @Expose
        var variant: List<Variant>? = null,
        @SerializedName("children")
        @Expose
        var children: List<Child>? = null,
        @SerializedName("sizechart")
        @Expose
        var sizechart: String? = null,
        @SerializedName("enabled")
        @Expose
        var enabled: Boolean? = null,
        @SerializedName("always_available")
        @Expose
        var alwaysAvailable: Boolean? = null,
        @SerializedName("stock")
        @Expose
        var stock: Int? = null
)

data class Picture(

        @SerializedName("original")
        @Expose
        var original: String? = null,
        @SerializedName("thumbnail")
        @Expose
        var thumbnail: String? = null

)