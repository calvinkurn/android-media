package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.variant.Picture

/**
 * Created by Yehezkiel on 01/07/20
 */
data class ProductP1VariantChild(
        @SerializedName("productID")
        @Expose
        val productId: String = "", //ex: 15212348

        @SerializedName("price")
        @Expose
        val price: Float = 0f, //ex: 100000000

        @SerializedName("priceFmt")
        @Expose
        val priceFmt: String? = null, //ex: Rp 100.000.000

        @SerializedName("sku")
        @Expose
        val sku: String? = null, // ex:gew2134

        @SerializedName("stock")
        @Expose
        val stock: ProductP1VariantStock? = null,

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

        //TODO check
        @SerializedName("campaignInfo")
        @Expose
        val campaign: ProductP1VariantCampaign? = null,

        @SerializedName("isWishlist")
        @Expose
        val isWishlist: Boolean? = null,

        @SerializedName("isCOD")
        @Expose
        val isCod: Boolean? = false,

        @SerializedName("warehouseInfo")
        @Expose
        val warehouseInfo: ProductP1VariantWarehouse? = null
)

data class ProductP1VariantWarehouse(
        @SerializedName("warehouseID")
        @Expose
        val warehouseId: String = "",

        @SerializedName("isFulfillment")
        @Expose
        val isFulfillment: Boolean = false,

        @SerializedName("districtID")
        @Expose
        val districtId: String = "",

        @SerializedName("postalCode")
        @Expose
        val postalCode: String = "",

        @SerializedName("geoLocation")
        @Expose
        val geoLocation: String = ""
)

data class ProductP1VariantStock(
        @SerializedName("stock")
        @Expose
        val stock: String? = "",

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
        val minimumOrder: String? = ""
)

data class ProductP1VariantCampaign(
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

        //TODO check
        @SerializedName("discountPercentage")
        @Expose
        val discountedPercentage: Float? = 0f,

        //TODO check
        @SerializedName("discountPrice")
        @Expose
        val discountedPrice: Float? = 0f,

        //TODO check
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
        val endDateUnix: Int? = null,

        @SerializedName("stockSoldPercentage")
        @Expose
        val stockSoldPercentage: Float? = null,

        @SerializedName("isUsingOvo")
        @Expose
        val isUsingOvo: Boolean = false,

        @SerializedName("isCheckImei")
        @Expose
        val isCheckImei: Boolean? = null
)