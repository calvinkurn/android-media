package com.tokopedia.variant_common.model

import androidx.collection.ArrayMap
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 08/03/20
 */

data class ProductDetailVariantCommonResponse(
        @SerializedName("getProductVariant")
        @Expose
        val data: ProductVariantCommon = ProductVariantCommon()
)

data class ProductVariantCommon(


        @SerializedName("errorCode")
        @Expose
        var errorCode: Int = 0,

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
        var children: List<VariantChildCommon> = listOf()
) {

    fun isSelectedChildHasFlashSale(optionId: Int): Boolean {
        var isFlashSale = false
        for (child: VariantChildCommon in children) {
            if (optionId == child.optionIds.firstOrNull()) {
                if (child.isFlashSale && child.isBuyable) {
                    isFlashSale = true
                    break
                }
            }
        }
        return isFlashSale
    }

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

    fun autoSelectedOptionIds(): List<Int> {
        val listOfOptionAutoSelectedId = children.filter {
            it.isBuyable
        }

        //If there is only 1 child is available , then auto selected
        return if (listOfOptionAutoSelectedId.size == 1) {
            listOfOptionAutoSelectedId.firstOrNull()?.optionIds ?: listOf()
        } else {
            listOf()
        }
    }

    fun getVariant(selectedVariantId: String?): VariantChildCommon? {
        if (selectedVariantId.isNullOrEmpty()) {
            return null
        }
        if (hasChildren) {
            for (child: VariantChildCommon in children) {
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

    private fun getChildProductVariant(selectedVariantId: String?): VariantChildCommon? {
        val variantId = selectedVariantId ?: defaultChildString
        if (hasChildren) {
            for (child: VariantChildCommon in children) {
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
        var thumbnail: String? = null

)