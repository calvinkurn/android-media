package com.tokopedia.tokopedianow.common.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.data.model.response.Header

data class GetProductAdsResponse(
    @SerializedName("displayAdsV3")
    val productAds: ProductAdsResponse = ProductAdsResponse()
) {

    data class ProductAdsResponse(
        @SerializedName("status")
        val status: Status = Status(),
        @SerializedName("header")
        val header: Header = Header(),
        @SerializedName("data")
        val productList: List<ProductAdsData> = emptyList()
    )

    data class ProductAdsData(
        @SerializedName("id")
        val id: String,
        @SerializedName("ad_ref_key")
        val adRefKey: String,
        @SerializedName("redirect")
        val redirect: String,
        @SerializedName("sticker_id")
        val stickerId: String,
        @SerializedName("sticker_image")
        val stickerImage: String,
        @SerializedName("product_click_url")
        val productClickUrl: String,
        @SerializedName("product_wishlist_url")
        val productWishlistUrl: String,
        @SerializedName("shop_click_url")
        val shopClickUrl: String,
        @SerializedName("tag")
        val tag: Int,
        @SerializedName("product")
        val product: Product,
        @SerializedName("shop")
        val shop: Shop,
        @SerializedName("applinks")
        val applinks: String
    )

    data class Shop(
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("domain")
        val domain: String,
        @SerializedName("location")
        val location: String,
        @SerializedName("city")
        val city: String,
        @SerializedName("gold_shop")
        val goldShop: Boolean,
        @SerializedName("gold_shop_badge")
        val goldShopBadge: Boolean,
        @SerializedName("lucky_shop")
        val luckyShop: String,
        @SerializedName("uri")
        val uri: String,
        @SerializedName("owner_id")
        val ownerId: String,
        @SerializedName("is_owner")
        val isOwner: Boolean,
        @SerializedName("shop_is_official")
        val shopIsOfficial: Boolean,
        @SerializedName("shop_rating_avg")
        val shopRatingAvg: String,
        @SerializedName("badges")
        val badges: List<Badges> = emptyList()
    )

    data class Badges(
        @SerializedName("title")
        val title: String,
        @SerializedName("image_url")
        val imageUrl: String,
        @SerializedName("show")
        val show: Boolean
    )

    data class Category(
        @SerializedName("id")
        val id: String
    )

    data class Campaign(
        @SerializedName("original_price")
        val originalPrice: String,
        @SerializedName("discount_percentage")
        val discountPercentage: Int
    ) {
        companion object {
            private const val PERCENTAGE = "%"
        }

        fun getDiscount(): String {
            return if (discountPercentage > 0) "$discountPercentage$PERCENTAGE" else ""
        }
    }

    data class Product(
        @SerializedName("id")
        val id: String,
        @SerializedName("name")
        val name: String,
        @SerializedName("category_breadcrumb")
        val categoryBreadcrumb: String,
        @SerializedName("wishlist")
        val wishlist: Boolean,
        @SerializedName("image")
        val image: Image,
        @SerializedName("uri")
        val uri: String,
        @SerializedName("relative_uri")
        val relativeUri: String,
        @SerializedName("price_format")
        val priceFormat: String,
        @SerializedName("price_range")
        val priceRange: String,
        @SerializedName("wholesale_price")
        val wholesalePrice: List<String> = emptyList(),
        @SerializedName("count_talk_format")
        val countTalkFormat: String,
        @SerializedName("count_review_format")
        val countReviewFormat: String,
        @SerializedName("category")
        val category: Category,
        @SerializedName("product_preorder")
        val productPreorder: Boolean,
        @SerializedName("product_wholesale")
        val productWholesale: Boolean,
        @SerializedName("free_return")
        val freeReturn: String,
        @SerializedName("product_cashback")
        val productCashback: Boolean,
        @SerializedName("product_new_label")
        val productNewLabel: Boolean,
        @SerializedName("product_cashback_rate")
        val productCashbackRate: String,
        @SerializedName("product_rating")
        val productRating: Int,
        @SerializedName("product_rating_format")
        val productRatingFormat: String,
        @SerializedName("product_item_sold_payment_verified")
        val productItemSoldPaymentVerified: String,
        @SerializedName("product_minimum_order")
        val productMinimumOrder: Int,
        @SerializedName("free_ongkir")
        val freeOngkir: FreeOngkir,
        @SerializedName("campaign")
        val campaign: Campaign,
        @SerializedName("label_group")
        val labelGroup: List<LabelGroup> = emptyList(),
        @SerializedName("customvideo_url")
        val customVideoUrl: String,
        @SerializedName("parent_id")
        val parentId: String,
        @SerializedName("max_order")
        val maxOrder: Int,
        @SerializedName("stock")
        val stock: Int
    )

    data class FreeOngkir(
        @SerializedName("is_active")
        val isActive: Boolean,
        @SerializedName("img_url")
        val imgUrl: String
    )

    data class Image(
        @SerializedName("m_ecs")
        val mEcs: String
    )

    data class LabelGroup(
        @SerializedName("position")
        val position: String,
        @SerializedName("type")
        val type: String,
        @SerializedName("title")
        val title: String,
        @SerializedName("url")
        val url: String
    )

    data class Status(
        @SerializedName("error_code")
        val errorCode: Int = 0,
        @SerializedName("message")
        val message: String = ""
    )
}
