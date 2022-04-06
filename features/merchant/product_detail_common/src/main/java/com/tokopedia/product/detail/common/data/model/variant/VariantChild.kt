package com.tokopedia.product.detail.common.data.model.variant

import androidx.collection.ArrayMap
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.product.detail.common.VariantConstant.DEFAULT_MAX_ORDER
import com.tokopedia.product.detail.common.data.model.pdplayout.ThematicCampaign

/**
 * Created by Yehezkiel on 01/07/20
 */
data class VariantChild(
        @SerializedName("productID")
        @Expose
        val productId: String = "", //ex: 15212348


        @SerializedName("price")
        @Expose
        val price: java.lang.Double = (0.0 as java.lang.Double), //ex: 100000000

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
        val optionIds: List<String> = listOf(),

        @SerializedName("optionName")
        @Expose
        val optionName: List<String> = listOf(),

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
        val campaign: VariantCampaign? = null,

        @SerializedName("campaignStatus")
        @Expose
        val campaignStatus: String? = null,

        @SerializedName("isCOD")
        @Expose
        val isCod: Boolean = false,

        @SerializedName("isWishlist")
        @Expose
        var isWishlist: Boolean = false,

        @SerializedName("thematicCampaign")
        val thematicCampaign: ThematicCampaign? = null
) {
    val finalMainPrice: Double
        get() {
            return if (campaign?.isActive == true) {
                campaign.originalPrice ?: (0.0 as Double)
            } else {
                price.toDouble()
            }
        }

    val finalPrice: Double
        get() {
            return if (campaign?.isActive == true) {
                campaign.discountedPrice ?: 0.0
            } else {
                price.toDouble()

            }
        }

    val slashPriceDouble: Double
        get() {
            return if (campaign?.isActive == true) {
                campaign.originalPrice ?: 0.0
            } else {
                0.0
            }
        }

    val discountPercentage: String
        get() {
            return if (campaign?.isActive == true) {
                campaign.discountedPercentage.toString()
            } else {
                ""
            }
        }

    val roundedDiscountPercentage: String
        get() {
            return if (campaign?.isActive == true) {
                campaign.discountedPercentage.toIntSafely().toString()
            } else {
                ""
            }
        }

    fun getFinalMinOrder(): Int = if (campaign?.isActive == true) campaign.minOrder
            ?: 0 else stock?.minimumOrder?.toIntOrNull() ?: 0

    fun getFinalMaxOrder(): Int = stock?.maximumOrder?.toIntOrNull() ?: DEFAULT_MAX_ORDER

    fun getVariantFinalStock(): Int {
        return if (campaign?.isActive == true) campaign.stock ?: 0 else stock?.stock ?: 0
    }

    val isBuyable: Boolean
        get() = getVariantFinalStock() > 0 && stock?.isBuyable ?: false

    val isInactive:Boolean
        get() = getVariantFinalStock() > 0 && stock?.isBuyable == false

    val isFlashSale: Boolean
        get() = campaign?.isActive == true

    val hasPicture: Boolean
        get() = picture != null &&
                (picture.original?.isNotEmpty() == true
                        || picture.thumbnail?.isNotEmpty() == true)

    fun getOptionStringList(variantReference: List<Variant>?): List<String> {
        if (variantReference != null && variantReference.isNotEmpty()) {
            val optionStringList = mutableListOf<String>()
            optionIds.forEachIndexed { index, option ->
                val value: String? = variantReference.getOrNull(index)?.options?.find { it.id == option }?.value
                value?.let {
                    optionStringList.add(it)
                }
            }
            return optionStringList
        }
        return listOf()
    }

    fun mapVariant(variants: List<Variant>?): ArrayMap<String, ArrayMap<String, String>>? {
        if (variants == null || variants.isEmpty()) return null
        val productVariantMap = initArrayMapVariant()
        for (optionId in optionIds) {
            for (variant in variants) {
                var isFound = false
                for (option in variant.options) {
                    if (option.id == optionId) {
                        productVariantMap[variant.identifier]?.set("value", option.value)
                        productVariantMap[variant.identifier]?.set("hex", option.hex)
                        productVariantMap[variant.identifier]?.set("id", option.id.toString())
                        isFound = true
                        break
                    }
                }
                if (isFound) break
            }
        }
        return productVariantMap
    }

    private fun initArrayMapVariant(): ArrayMap<String, ArrayMap<String, String>> {
        return ArrayMap<String, ArrayMap<String, String>>().apply {
            put("colour", ArrayMap())
            put("size", ArrayMap())
        }
    }
}

data class VariantStock(
        @SerializedName("stock")
        @Expose
        val stock: Int? = 0,

        @SerializedName("isBuyable")
        @Expose
        val isBuyable: Boolean? = false,

        @SerializedName("stockWording")
        @Expose
        val stockWording: String? = "",

        @SerializedName("stockWordingHTML")
        @Expose
        val stockWordingHTML: String? = "",

        @SerializedName("minimumOrder")
        @Expose
        val minimumOrder: String? = "",

        @SerializedName("maximumOrder")
        @Expose
        val maximumOrder: String? = "",

        @SerializedName("stockFmt")
        @Expose
        val stockFmt: String? = "", // Stock: 11 or >50

        @SerializedName("stockCopy")
        @Expose
        val stockCopy: String? = ""

)

data class VariantCampaign(
        @SerializedName("campaignID")
        @Expose
        val campaignID: String? = "",

        @SerializedName("isActive")
        @Expose
        val isActive: Boolean? = null,

        @SerializedName("originalPrice")
        @Expose
        val originalPrice: Double? = null,

        @SerializedName("originalPriceFmt")
        @Expose
        val originalPriceFmt: String? = null,

        @SerializedName("discountPercentage")
        @Expose
        val discountedPercentage: Float? = 0f,

        @SerializedName("discountPrice")
        @Expose
        val discountedPrice: Double? = 0.0,

        @SerializedName("discountPriceFmt")
        @Expose
        val discountedPriceFmt: String? = null,

        @SerializedName("campaignType")
        @Expose
        val campaignType: String? = null,

        @SerializedName("campaignTypeName")
        @Expose
        val campaignTypeName: String? = null,

        @SerializedName("startDate")
        @Expose
        val startDate: String? = null,

        @SerializedName("stock")
        @Expose
        val stock: Int? = null,

        @SerializedName("isAppsOnly")
        @Expose
        val isAppsOnly: Boolean? = null,

        @SerializedName("appLinks")
        @Expose
        val applinks: String? = null,

        @SerializedName("endDateUnix")
        @Expose
        val endDateUnix: String? = null,

        @SerializedName("stockSoldPercentage")
        @Expose
        val stockSoldPercentage: Float? = null,

        @SerializedName("isUsingOvo")
        @Expose
        val isUsingOvo: Boolean = false,

        @SerializedName("isCheckImei")
        @Expose
        val isCheckImei: Boolean? = null,

        @SerializedName("minOrder")
        @Expose
        val minOrder: Int? = null,

        @SerializedName("hideGimmick")
        @Expose
        val hideGimmick: Boolean? = null,

        @SerializedName("campaignIdentifier")
        val campaignIdentifier: Int = 0,

        @SerializedName("background")
        val background: String = ""
)