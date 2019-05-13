package com.tokopedia.product.detail.common.data.model.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductDetailVariantResponse(
        @SerializedName("getProductVariant")
        @Expose
        val data: ProductVariant = ProductVariant()
)

data class ProductVariant(

        @SerializedName("parentID")
        @Expose
        var parentId: Int = 0,

        @SerializedName("defaultChild")
        @Expose
        var defaultChild: Int = 0,

        @SerializedName("sizeChart")
        @Expose
        var sizeChart: String = "",

        @SerializedName("alwaysAvailable")
        @Expose
        var alwaysAvailable: Boolean? = null,

        @SerializedName("stock")
        @Expose
        var stock: Int? = null,

        @SerializedName("variant")
        @Expose
        var variant: List<Variant> = listOf(),

        @SerializedName("children")
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

    fun getSelectedProductVariantChild(selectedVariantId: String?): Array<Array<String?>>? {
        return getVariant(selectedVariantId)?.getSelectedVariant(variant)
    }
}

data class Picture(

        @SerializedName("url")
        @Expose
        var original: String? = null,
        @SerializedName("url200")
        @Expose
        var thumbnail: String? = null

)