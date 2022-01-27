package com.tokopedia.play.broadcaster.domain.model.campaign

import com.google.gson.annotations.SerializedName

/**
 * Created by meyta.taliti on 25/01/22.
 */
data class GetCampaignProductResponse(
    @SerializedName("getCampaignProduct")
    val getCampaignProduct: GetCampaignProduct = GetCampaignProduct()
) {

    data class GetCampaignProduct(
        @SerializedName("Products")
        val products: List<Product> = listOf(),

        @SerializedName("TotalProduct")
        val totalProduct: Int = 0,

        @SerializedName("PaymentProfile")
        val paymentProfile: String = "",
    )

    data class Product(
        @SerializedName("ID")
        val id: Long = 0,

        @SerializedName("name")
        val name: String = "",

        @SerializedName("URL")
        val url: String = "",

        @SerializedName("URLApps")
        val urlApps: String = "",

        @SerializedName("URLMobile")
        val urlMobile: String = "",

        @SerializedName("imageUrl")
        val imageUrl: String = "",

        @SerializedName("imageURL700")
        val imageURL700: String = "",

        @SerializedName("price")
        val price: String = "",

        @SerializedName("shop")
        val shop: PrdCmpShop = PrdCmpShop(),

        @SerializedName("wholesale")
        val wholesale: List<PrdCmpWholesale> = listOf(),

        @SerializedName("courierCount")
        val courierCount: Int = 0,

        @SerializedName("condition")
        val condition: Int = 0,

        @SerializedName("departmentID")
        val departmentID: List<Int> = listOf(),

        @SerializedName("labels")
        val labels: List<PrdCmpLabel> = listOf(),

        @SerializedName("badges")
        val badges: List<PrdCmpBadge> = listOf(),

        @SerializedName("rating")
        val rating: Float = 0F,

        @SerializedName("starRating")
        val starRating: Float = 0F,

        @SerializedName("countReview")
        val countReview: Int = 0,

        @SerializedName("countSold")
        val countSold: Int = 0,

        @SerializedName("SKU")
        val sku: String = "",

        @SerializedName("stock")
        val stock: Int = 0,

        @SerializedName("campaign")
        val campaign: PrdCmpInfo = PrdCmpInfo(),

        @SerializedName("returnable")
        val returnable: Int = 0,

        @SerializedName("status")
        val status: Int = 0,

        @SerializedName("hasCashback")
        val hasCashback: Boolean = false,

        @SerializedName("cashbackAmount")
        val cashbackAmount: Float = 0F,

        @SerializedName("map_id")
        val mapId: String = "",

        @SerializedName("lockStatus")
        val lockStatus: Int = 0,

        @SerializedName("maxOrder")
        val maxOrder: Int = 0,

        @SerializedName("priceUnfmt")
        val priceUnfmt: String = "",

        @SerializedName("isVariant")
        val isVariant: Boolean = false,

        @SerializedName("parentId")
        val parentId: String = "",

        @SerializedName("variantsFilter")
        val variantsFilter: List<String> = listOf(),

        @SerializedName("childIds")
        val childIds: List<Int> = listOf(),

        @SerializedName("siblingIds")
        val siblingIds: List<Int> = listOf(),

        @SerializedName("eggCrackingValidation")
        val eggCrackingValidation: Boolean = false,

        @SerializedName("min_order")
        val minOrder: Int = 0
    )

    data class PrdCmpShop(

        @SerializedName("ShopID")
        val shopId: String = "",

        @SerializedName("Name")
        val name: String = "",

        @SerializedName("URL")
        val url: String = "",

        @SerializedName("URLMobile")
        val urlMobile: String = "",

        @SerializedName("URLApps")
        val urlApps: String = "",

        @SerializedName("IsGold")
        val isGold: Boolean = false,

        @SerializedName("IsOfficial")
        val isOfficial: Boolean = false,
    )

    data class PrdCmpWholesale(

        @SerializedName("QuantityMin")
        val quantityMin: Int = 0,

        @SerializedName("QuantityMax")
        val quantityMax: Int = 0,

        @SerializedName("Price")
        val price: Float = 0F,
    )

    data class PrdCmpLabel(

        @SerializedName("Title")
        val title: String = "",

        @SerializedName("Color")
        val color: String = "",
    )

    data class PrdCmpBadge(

        @SerializedName("Title")
        val title: String = "",

        @SerializedName("ImageURL")
        val imageURL: String = "",
    )

    data class PrdCmpInfo(

        @SerializedName("CampaignID")
        val campaignID: Int = 0,

        @SerializedName("GroupdID")
        val groupdID: List<Int> = listOf(),

        @SerializedName("DiscountPercentage")
        val discountPercentage: Float = 0F,

        @SerializedName("DiscountedPrice")
        val discountedPrice: String = "",

        @SerializedName("OriginalPrice")
        val originalPrice: String = "",

        @SerializedName("Cashback")
        val cashback: Int = 0,

        @SerializedName("CustomStock")
        val customStock: Int = 0,

        @SerializedName("StockSoldPercentage")
        val stockSoldPercentage: Float = 0F,

        @SerializedName("CampaignStatus")
        val campaignStatus: Int = 0,

        @SerializedName("ProductSystemStatus")
        val productSystemStatus: Int = 0,

        @SerializedName("ProductAdminStatus")
        val productAdminStatus: Int = 0,

        @SerializedName("StartDate")
        val startDate: String = "",

        @SerializedName("EndDate")
        val endDate: String = "",

        @SerializedName("CampaignTypeName")
        val campaignTypeName: String = "",

        @SerializedName("CampaignShortName")
        val CampaignShortName: String = "",

        @SerializedName("MaxOrder")
        val maxOrder: Int = 0,

        @SerializedName("OriginalMaxOrder")
        val originalMaxOrder: Int = 0,

        @SerializedName("DiscountedPriceFmt")
        val discountedPriceFmt: String = "",

        @SerializedName("OriginalPriceFmt")
        val originalPriceFmt: String = "",

        @SerializedName("OriginalStock")
        val originalStock: Int = 0,

        @SerializedName("OriginalStockStatus")
        val originalStockStatus: Int = 0,

        @SerializedName("CampaignSoldCount")
        val campaignSoldCount: Int = 0,

        @SerializedName("OriginalCustomStock")
        val originalCustomStock: Int = 0,

        @SerializedName("AppsOnly")
        val appsOnly: Boolean = false,

        @SerializedName("Applinks")
        val appLink: String = "",

        @SerializedName("FinalPrice")
        val finalPrice: Int = 0,

        @SerializedName("SellerPrice")
        val sellerPrice: Int = 0,

        @SerializedName("TokopediaSubsidy")
        val tokopediaSubsidy: Int = 0,

        @SerializedName("BookingStock")
        val bookingStock: Int = 0,

        @SerializedName("IsBigCampaign")
        val isBigCampaign: Boolean = false,

        @SerializedName("MinOrder")
        val minOrder: Int = 0,

        @SerializedName("RedirectPageUrl")
        val redirectPageUrl: String = "",

        @SerializedName("RedirectPageApplink")
        val redirectPageApplink: String = "",

        @SerializedName("SpoilerPriceFmt")
        val spoilerPriceFmt: String = "",

        @SerializedName("SpoilerPrice")
        val spoilerPrice: String = "",

        @SerializedName("HideGimmick")
        val hideGimmick: Boolean = false,
    )
}