package com.tokopedia.product.detail.common.data.model.variant

import androidx.collection.ArrayMap
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
        get() = with(children) { this.isNotEmpty() }

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

    fun mapSelectedProductVariants(selectedVariantId: String?): ArrayMap<String, ArrayMap<String, String>>? {
        val child = getChildProductVariant(selectedVariantId)
        return child?.mapVariant(variant)
    }

    private fun getChildProductVariant(selectedVariantId: String?): Child? {
        val variantId = selectedVariantId ?: defaultChildString
        if (hasChildren) {
            for (child: Child in children) {
                if (child.productId.toString().equals(variantId, false)) {
                    return child
                }
            }
        }
        return null
    }


}

data class Picture(

        @SerializedName("url")
        @Expose
        var original: String? = null,
        @SerializedName("url200")
        @Expose
        var thumbnail: String? = null,
        @SerializedName("url100")
        @Expose
        var url100: String? = null

)