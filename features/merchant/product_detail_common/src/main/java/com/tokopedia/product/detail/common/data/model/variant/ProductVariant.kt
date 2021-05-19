package com.tokopedia.product.detail.common.data.model.variant

import androidx.collection.ArrayMap
import com.google.gson.annotations.SerializedName

/**
 * Created by Yehezkiel on 04/05/21
 */

data class ProductVariant(
        @SerializedName("parentID")
        val parentId: String = "",
        @SerializedName("errorCode")
        val errorCode: Int = 0,
        @SerializedName("sizeChart")
        val sizeChart: String = "",
        @SerializedName("defaultChild")
        val defaultChild: String = "",
        @SerializedName(value = "variants", alternate = ["variant"])
        val variants: List<Variant> = listOf(),
        @SerializedName("children")
        val children: List<VariantChild> = listOf()
){
        fun getChildByOptionId(selectedIds: List<String>): VariantChild? {
                var childResult: VariantChild? = null
                for (it: VariantChild in children) {
                        if (it.optionIds == selectedIds) {
                                childResult = it
                                break
                        }
                }
                return childResult
        }

        fun getChildByProductId(selectedVariantId: String?): VariantChild? {
                if (selectedVariantId.isNullOrEmpty()) {
                        return null
                }
                if (hasChildren) {
                        for (child: VariantChild in children) {
                                if (child.productId.equals(selectedVariantId, false)) {
                                        return child
                                }
                        }
                }
                return null
        }

        fun getVariantsIdentifier(): String {
                return if (variants.any { it.name == null }) "" else variants.joinToString(" & ") { it.name.toString() }
        }

        fun isSelectedChildHasFlashSale(optionId: String): Boolean {
                var isFlashSale = false
                for (child: VariantChild in children) {
                        if (optionId == child.optionIds.firstOrNull()) {
                                if (child.isFlashSale && child.isBuyable) {
                                        isFlashSale = true
                                        break
                                }
                        }
                }
                return isFlashSale
        }

        fun getBuyableVariantCount(): Int {
                return children.filter{ it.isBuyable }.count()
        }

        val totalStockChilds : Int
                get() = children.sumOf {
                        it.getVariantFinalStock()
                }

        val hasChildren: Boolean
                get() = with(children) { this.isNotEmpty() }

        val hasVariant: Boolean
                get() = with(variants) { this.isNotEmpty() }

        val defaultChildString: String?
                get() = if (defaultChild.toLongOrNull() ?: 0L != 0L) {
                        defaultChild
                } else {
                        null
                }

        fun autoSelectedOptionIds(): List<String> {
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

        fun getOptionListString(selectedVariantId: String?): List<String>? {
                return getChildByProductId(selectedVariantId)?.getOptionStringList(variants)
        }

        fun mapSelectedProductVariants(selectedVariantId: String?): ArrayMap<String, ArrayMap<String, String>>? {
                val child = getChildProductVariant(selectedVariantId)
                return child?.mapVariant(variants)
        }

        private fun getChildProductVariant(selectedVariantId: String?): VariantChild? {
                val variantId = selectedVariantId ?: defaultChildString
                if (hasChildren) {
                        for (child: VariantChild in children) {
                                if (child.productId.equals(variantId, false)) {
                                        return child
                                }
                        }
                }
                return null
        }
}