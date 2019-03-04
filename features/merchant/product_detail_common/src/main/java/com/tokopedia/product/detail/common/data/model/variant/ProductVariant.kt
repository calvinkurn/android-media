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
        var sizeChart: String = "",

        @SerializedName("AlwaysAvailable")
        @Expose
        var alwaysAvailable: Boolean? = null,

        @SerializedName("Stock")
        @Expose
        var stock: Int? = null,

        @SerializedName("Variant")
        @Expose
        var variant: List<Variant> = listOf(),

        @SerializedName("Children")
        @Expose
        var children: List<Child> = listOf()
) {
    val hasChildren: Boolean
        get() = with(children) {this.isNotEmpty() }

    val hasVariant: Boolean
        get() = with(variant) { this.isNotEmpty() }

    val defaultChildString: String?
        get() = if (defaultChild != null && defaultChild != 0) {
            defaultChild.toString()
        } else {
            null
        }

    fun getVariant(selectedVariantId: String?): Child? {
        if (selectedVariantId.isNullOrEmpty()) {
            return null
        }
        if (hasChildren) {
            for (child: Child in children) {
                if (child.productId.toString().equals(selectedVariantId, false)) {
                    return child
                }
            }
        }
        return null
    }

    fun getOptionListString(selectedVariantId: String?): List<String>? {
        return getVariant(selectedVariantId)?.getOptionStringList(variant)
    }
}

data class Picture(

        @SerializedName("URL")
        @Expose
        var original: String? = null,
        @SerializedName("URL200")
        @Expose
        var thumbnail: String? = null

)