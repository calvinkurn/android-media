package com.tokopedia.product.detail.common.data.model.variant

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
    @SerializedName("maxFinalPrice")
    val maxFinalPrice: Float = 0F,
    @SerializedName(value = "variants", alternate = ["variant"])
    val variants: List<Variant> = listOf(),
    @SerializedName("children")
    val children: List<VariantChild> = listOf(),
    /**
     * used when landing on pdp, if it is empty use hardcode FE
     * and if there’s a user activity for choosing the variant, use children.subText below
     * Details: https://tokopedia.atlassian.net/wiki/spaces/PDP/pages/2245002923/PDP+P1+Product+Variant+Partial+OOS
     */
    @SerializedName("landingSubText")
    val landingSubText: String = ""
) {
    fun isOneOfTheChildBuyablePartial(optionId: String): Boolean {
        var result = false
        for (child in children) {
            if (optionId in child.optionIds && child.isBuyable) {
                result = true
                break
            }
        }
        return result
    }

    fun isOneOfTheChildBuyable(optionId: List<String>): Boolean {
        var result = false
        for (child in children) {
            if (optionId.containsAll(child.optionIds) && child.isBuyable) {
                result = true
                break
            }
        }
        return result
    }

    // Hitam,M
    fun getVariantCombineIdentifier(): String {
        val list = variants.mapIndexed { index, variant ->
            "${variants.getOrNull(index)?.options?.size ?: ""} ${variant.name}"
        }
        return list.joinToString() + "."
    }

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

    fun autoSelectIfParent(selectedVariantId: String?): VariantChild? {
        val child = getChildByProductId(selectedVariantId)

        return child ?: children.firstOrNull { it.isBuyable } ?: children.firstOrNull()
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
        return children.filter { it.isBuyable }.count()
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

    fun getVariantGuideline(
        sizeIdentifier: Boolean
    ): String = if (sizeIdentifier && sizeChart.isNotEmpty()) sizeChart else ""

    fun getOptionListString(selectedVariantId: String?): List<String>? {
        return getChildByProductId(selectedVariantId)?.getOptionStringList(variants)
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
