package com.tokopedia.home.beranda.domain.gql.feed

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Product(
        @SerializedName("id") @Expose
        var id: String = "",
        @SerializedName("name")
        @Expose
        val name: String = "",
        @SerializedName("category_breadcrumbs")
        @Expose
        val categoryBreadcrumbs: String = "",
        @SerializedName("url")
        @Expose
        val url: String = "",
        @SerializedName("click_url")
        @Expose
        val clickUrl: String = "",
        @SerializedName("image_url")
        @Expose
        val imageUrl: String = "",
        @SerializedName("is_wishlist")
        @Expose
        val isWishlist: Boolean = false,
        @SerializedName("wishlist_url")
        @Expose
        val wishlistUrl: String = "",
        @SerializedName("applink")
        @Expose
        val applink: String = "",
        @SerializedName("is_topads")
        @Expose
        val isTopads: Boolean = false,
        @SerializedName("tracker_image_url")
        @Expose
        val trackerImageUrl: String = "",
        @SerializedName("price")
        @Expose
        val price: String = "",
        @SerializedName("price_int")
        @Expose
        val priceInt: Int = 0,
        @SerializedName("slashed_price")
        @Expose
        val slashedPrice: String = "",
        @SerializedName("slashed_price_int")
        @Expose
        val slashedPriceInt: Int = 0,
        @SerializedName("discount_percentage")
        @Expose
        val discountPercentage: Int = 0,
        @SerializedName("clusterID")
        @Expose
        val clusterId: Int = -1,
        @SerializedName("rating")
        @Expose
        val rating: Int = 0,
        @SerializedName("count_review")
        @Expose
        val countReview: Int = 0,
        @SerializedName("ratingAverage")
        @Expose
        val ratingFloat: String = "",
        @SerializedName("recommendation_type")
        @Expose
        val recommendationType: String = "",
        @SerializedName("labels")
        @Expose
        val labels: List<Label> = listOf(),
        @SerializedName("label_group")
        @Expose
        val labelGroup: List<LabelGroup> = listOf(),
        @SerializedName("badges")
        @Expose
        val badges: List<Badge> = listOf(),
        @SerializedName("shop")
        @Expose
        val shop: Shop = Shop(),
        @SerializedName("free_ongkir")
        @Expose
        val freeOngkirInformation: FreeOngkirInformation = FreeOngkirInformation()
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (id != other.id) return false
        if (name != other.name) return false
        if (categoryBreadcrumbs != other.categoryBreadcrumbs) return false
        if (url != other.url) return false
        if (clickUrl != other.clickUrl) return false
        if (imageUrl != other.imageUrl) return false
        if (isWishlist != other.isWishlist) return false
        if (wishlistUrl != other.wishlistUrl) return false
        if (applink != other.applink) return false
        if (isTopads != other.isTopads) return false
        if (trackerImageUrl != other.trackerImageUrl) return false
        if (price != other.price) return false
        if (priceInt != other.priceInt) return false
        if (slashedPrice != other.slashedPrice) return false
        if (slashedPriceInt != other.slashedPriceInt) return false
        if (discountPercentage != other.discountPercentage) return false
        if (rating != other.rating) return false
        if (countReview != other.countReview) return false
        if (recommendationType != other.recommendationType) return false
        if (labels != other.labels) return false
        if (labelGroup != other.labelGroup) return false
        if (badges != other.badges) return false
        if (shop != other.shop) return false
        if (freeOngkirInformation != other.freeOngkirInformation) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + categoryBreadcrumbs.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + clickUrl.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + isWishlist.hashCode()
        result = 31 * result + wishlistUrl.hashCode()
        result = 31 * result + applink.hashCode()
        result = 31 * result + isTopads.hashCode()
        result = 31 * result + trackerImageUrl.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + priceInt
        result = 31 * result + slashedPrice.hashCode()
        result = 31 * result + slashedPriceInt
        result = 31 * result + discountPercentage
        result = 31 * result + rating
        result = 31 * result + countReview
        result = 31 * result + recommendationType.hashCode()
        result = 31 * result + labels.hashCode()
        result = 31 * result + labelGroup.hashCode()
        result = 31 * result + badges.hashCode()
        result = 31 * result + shop.hashCode()
        result = 31 * result + freeOngkirInformation.hashCode()
        return result
    }
}