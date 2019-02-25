package com.tokopedia.product.detail.common.data.model.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductDetailVariantResponse(
        @SerializedName("GetProductVariant")
        @Expose
        val data: ProductVariant? = ProductVariant()
)

data class ProductVariant(

        @SerializedName("ParentID")
        @Expose
        var parentId: Int? = null,

        @SerializedName("DefaultChild")
        @Expose
        var defaultChild: Int? = null,

        @SerializedName("SizeChart")
        @Expose
        var sizechart: String? = null,

        @SerializedName("Enabled")
        @Expose
        var enabled: Boolean? = null,

        @SerializedName("AlwaysAvailable")
        @Expose
        var alwaysAvailable: Boolean? = null,

        @SerializedName("Stock")
        @Expose
        var stock: Int? = null,

        @SerializedName("Variant")
        @Expose
        var variant: List<Variant>? = null,

        @SerializedName("Children")
        @Expose
        var children: List<Child>? = null
) {
        val hasChildren: Boolean
                get() = with(children) {this!= null && this.isNotEmpty() }

        val hasVariant: Boolean
                get() = with(variant) {this!= null && this.isNotEmpty() }
}

data class Picture(

        @SerializedName("URL")
        @Expose
        var original: String? = null,
        @SerializedName("URL200")
        @Expose
        var thumbnail: String? = null

)