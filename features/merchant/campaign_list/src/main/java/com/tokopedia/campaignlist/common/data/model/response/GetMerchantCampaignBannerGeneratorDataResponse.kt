package com.tokopedia.campaignlist.common.data.model.response

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class GetMerchantCampaignBannerGeneratorDataResponse(
        @SerializedName("getMerchantCampaignBannerGeneratorData")
        @Expose val getMerchantCampaignBannerGeneratorData: GetMerchantCampaignBannerGeneratorData = GetMerchantCampaignBannerGeneratorData()
)

data class GetMerchantCampaignBannerGeneratorData(
        @SerializedName("response_header")
        @Expose val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("campaign")
        @Expose val campaign: Campaign = Campaign(),
        @SerializedName("shop_data")
        @Expose val shopData: ShopData = ShopData(),
        @SerializedName("formatted_start_date")
        @Expose val formattedStartDate: String = "",
        @SerializedName("formatted_end_date")
        @Expose val formattedEndDate: String = "",
        @SerializedName("formatted_review_end_date")
        @Expose val formattedReviewEndDate: String = "",
        @SerializedName("formatted_sharing_start_date")
        @Expose val formattedSharingStartDate: String = "",
        @SerializedName("formatted_sharing_end_date")
        @Expose val formattedSharingEndDate: String = "",
        @SerializedName("formatted_sharing_review_end_date")
        @Expose val formattedSharingReviewEndDate: String = ""
)

data class ResponseHeader(
        @SerializedName("status")
        @Expose val status: String = "",
        @SerializedName("success")
        @Expose val success: Boolean = false,
        @SerializedName("processTime")
        @Expose val processTime: Double = 0.0
)

data class Campaign(
        @SerializedName("campaign_id")
        @Expose val campaignId: String = "",
        @SerializedName("name")
        @Expose val name: String = "",
        @SerializedName("start_date")
        @Expose val startDate: String = "",
        @SerializedName("end_date")
        @Expose val endDate: String = "",
        @SerializedName("review_end_date")
        @Expose val reviewEndDate: String = "",
        @SerializedName("status_text")
        @Expose val statusText: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("status_id")
        @Expose val statusId: Int = 0,
        @SerializedName("total_product")
        @Expose val totalProduct: String = "",
        @SerializedName("total_product_overload")
        @Expose val totalProductOverload: String = "",
        @SerializedName("discount_percentage_text")
        @Expose val discountPercentageText: String = "",
        @SerializedName("highlight_products")
        @Expose val highlightProducts: HighlightProducts = HighlightProducts()
)

data class ShopData(
        @SerializedName("ShopID")
        @Expose val shopId: String = "",
        @SerializedName("Name")
        @Expose val name: String = "",
        @SerializedName("URL")
        @Expose val url: String = "",
        @SerializedName("URLMobile")
        @Expose val urlMobile: String = "",
        @SerializedName("URLApps")
        @Expose val urlApps: String = "",
        @SerializedName("IsGold")
        @Expose val isGold: Boolean = false,
        @SerializedName("IsOfficial")
        @Expose val isOfficial: Boolean = false,
        @SerializedName("Logo")
        @Expose val logo: String = "",
        @SerializedName("City")
        @Expose val city: String = "",
        @SerializedName("Domain")
        @Expose val domain: String = "",
        @SerializedName("UserID")
        @Expose val userId: String = "",
        @SerializedName("Badge")
        @Expose val badge: Badge = Badge()

)

data class HighlightProducts(
        @SerializedName("wording")
        @Expose val wording: String = "",
        @SerializedName("total_product_wording")
        @Expose val totalProductWording: String = "",
        @SerializedName("Products")
        @Expose val Products: List<Products> = listOf()
)

data class Badge(
        @SerializedName("Title")
        @Expose val Title: String = "",
        @SerializedName("ImageURL")
        @Expose val ImageURL: String = ""
)

data class Products(
        @SerializedName("ID")
        @Expose val id: String = "",
        @SerializedName("name")
        @Expose val name: String = "",
        @SerializedName("URL")
        @Expose val url: String = "",
        @SerializedName("URLApps")
        @Expose val urlApps: String = "",
        @SerializedName("URLMobile")
        @Expose val urlMobile: String = "",
        @SerializedName("imageUrl")
        @Expose val imageUrl: String = "",
        @SerializedName("imageURL700")
        @Expose val imageUrl700: String = "",
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        @Expose val price: String = "",
        @SerializedName("courierCount")
        @Expose val courierCount: Int = 0,
        @SerializedName("condition")
        @Expose val condition: Int = 0,
        @SerializedName("rating")
        @Expose val rating: Int = 0,
        @SerializedName("starRating")
        @Expose val starRating: Int = 0,
        @SerializedName("countReview")
        @Expose val countReview: Int = 0,
        @SerializedName("countSold")
        @Expose val countSold: Int = 0,
        @SerializedName("SKU")
        @Expose val sku: String = "",
        @SerializedName("stock")
        @Expose val stock: Int = 0,
        @SerializedName("returnable")
        @Expose val returnable: Int = 0,
        @SerializedName("status")
        @Expose val status: Int = 0,
        @SerializedName("hasCashback")
        @Expose val hasCashback: Boolean = false,
        @SerializedName("cashbackAmount")
        @Expose val cashbackAmount: Int = 0,
        @SerializedName("map_id")
        @Expose val mapId: String = "",
        @SerializedName("lockStatus")
        @Expose val lockStatus: Int = 0,
        @SerializedName("maxOrder")
        @Expose val maxOrder: Int = 0,
        @SerializedName("priceUnfmt")
        @Expose val priceUnfmt: String = "",
        @SerializedName("isVariant")
        @Expose val isVariant: Boolean = false,
        @SerializedName("parentId")
        @Expose val parentId: String = "",
        @SerializedName("eggCrackingValidation")
        @Expose val eggCrackingValidation: Boolean = false,
        @SerializedName("min_order")
        @Expose val minOrder: Int = 0,
        @SerializedName("campaign")
        @Expose val productCampaign: ProductCampaignData = ProductCampaignData()
)

data class ProductCampaignData(
        @SerializedName("CampaignID")
        @Expose val campaignId: String = "",
        @SerializedName("DiscountPercentage")
        @Expose val discountPercentage: Int = 0,
        @SerializedName("DiscountedPrice")
        @Expose val discountedPrice: String = "",
        @SerializedName("OriginalPrice")
        @Expose val originalPrice: String = "",
        @SerializedName("DiscountedPriceFmt")
        @Expose val discountedPriceFmt: String = "",
        @SerializedName("OriginalPriceFmt")
        @Expose val originalPriceFmt: String = ""
)

