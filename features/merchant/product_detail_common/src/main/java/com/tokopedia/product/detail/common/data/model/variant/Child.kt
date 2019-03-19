package com.tokopedia.product.detail.common.data.model.variant

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by hendry on 01/02/19.
 */
data class Child(

    @SerializedName("productID")
    @Expose
    val productId: Int? = null, //ex: 15212348

    @SerializedName("price")
    @Expose
    val price: Float? = null, //ex: 100000000

    @SerializedName("priceFmt")
    @Expose
    val priceFmt: String? = null, //ex: Rp 100.000.000

    @SerializedName("sku")
    @Expose
    val sku: String? = null, // ex:gew2134

    @SerializedName("stock")
    @Expose
    val stock: VariantStock? = null,

    @SerializedName("optionID")
    @Expose
    val optionIds: List<Int> = listOf(),

    @SerializedName("productName")
    @Expose
    val name: String = "",

    @SerializedName("productURL")
    @Expose
    val url: String? = null,

    @SerializedName("picture")
    @Expose
    val picture: Picture? = null,

    @SerializedName("campaignInfo")
    @Expose
    val campaign: Campaign? = null,

    @SerializedName("isWishlist")
    @Expose
    val isWishlist: Boolean? = null,

    @SerializedName("isCOD")
    @Expose
    val isCod: Boolean? = false
) {
    val isBuyable: Boolean
        get() = stock?.isBuyable ?: false

    val hasPicture: Boolean
        get() = picture != null &&
            (picture.original?.isNotEmpty() == true
                || picture.thumbnail?.isNotEmpty() == true)

    fun getOptionStringList(variantReference: List<Variant>?): List<String> {
        if (variantReference != null && variantReference.isNotEmpty()) {
            val optionStringList = mutableListOf<String>()
            optionIds.forEachIndexed { index, option ->
                val value:String? = variantReference.get(index).options.find { it.id == option }?.value
                value?.let {
                    optionStringList.add(it)
                }
            }
            return optionStringList
        }
        return listOf()
    }
}

data class Campaign(

    @SerializedName("campaignID")
    @Expose
    val campaignID: String? = "",

    @SerializedName("isActive")
    @Expose
    val isActive: Boolean? = null,

    @SerializedName("originalPrice")
    @Expose
    val originalPrice: Float? = null,

    @SerializedName("originalPriceFmt")
    @Expose
    val originalPriceFmt: String? = null,

    @SerializedName("discountPercentage")
    @Expose
    val discountedPercentage: Float? = 0f,

    @SerializedName("discountPrice")
    @Expose
    val discountedPrice: Float? = 0f,

    @SerializedName("discountPriceFmt")
    @Expose
    val discountedPriceFmt: String? = null,

    @SerializedName("campaignType")
    @Expose
    val campaignType: Int? = null,

    @SerializedName("campaignTypeName")
    @Expose
    val campaignTypeName: String? = null,

    @SerializedName("startDate")
    @Expose
    val startDate: String? = null,

    @SerializedName("endDate")
    @Expose
    val endDate: String? = null,

    @SerializedName("stock")
    @Expose
    val stock: Int? = null,

    @SerializedName("isAppsOnly")
    @Expose
    val isAppsOnly: Boolean? = null,

    @SerializedName("appLinks")
    @Expose
    val applinks: String? = null
) {
    val activeAndHasId: Boolean
        get() = isActive == true && (campaignID?.isNotEmpty() == true)
}


data class VariantStock(
    @SerializedName("stock")
    @Expose
    val stock: Int? = 0,

    @SerializedName("isBuyable")
    @Expose
    val isBuyable: Boolean? = false,

    @SerializedName("alwaysAvailable")
    @Expose
    val alwaysAvailable: Boolean? = false,

    @SerializedName("isLimitedStock")
    @Expose
    val isLimitedStock: Boolean? = false,

    @SerializedName("stockWording")
    @Expose
    val stockWording: String? = "",

    @SerializedName("stockWordingHTML")
    @Expose
    val stockWordingHTML: String? = "",

    @SerializedName("otherVariantStock")
    @Expose
    val otherVariantStock: String? = "",

    @SerializedName("minimumOrder")
    @Expose
    val minimumOrder: Int? = 0,

    @SerializedName("maximumOrder")
    @Expose
    val maximumOrder: Int? = 0
)