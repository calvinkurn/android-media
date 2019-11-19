package com.tokopedia.home_wishlist.model.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.sdk.domain.model.ImpressHolder


data class Badge (
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("image_url")
    @Expose
    val imageUrl: String

) {
    override fun equals(other: Any?): Boolean {
        return other is Badge && title == other.title && imageUrl == other.imageUrl
    }

    override fun hashCode(): Int {
        var result = title.hashCode()
        result = 31 * result + imageUrl.hashCode()
        return result
    }
}

data class WishlistResponse (
    @SerializedName("wishlist")
    @Expose
    val wishlist: Wishlist
)

data class FreeOngkir (
    @SerializedName("is_active")
    @Expose
    val isActive: Boolean = false,
    @SerializedName("image_url")
    @Expose
    val imageUrl: String = ""
){
    override fun equals(other: Any?): Boolean {
        return other is FreeOngkir && isActive == other.isActive && imageUrl == other.imageUrl
    }
}

data class WishlistItem(
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("url")
    @Expose
    val url: String = "",
    @SerializedName("image_url")
    @Expose
    val imageUrl: String = "",
    @SerializedName("raw_price")
    @Expose
    val rawPrice: Int = -1,
    @SerializedName("condition")
    @Expose
    val condition: String = "",
    @SerializedName("available")
    @Expose
    val available: Boolean = false,
    @SerializedName("status")
    @Expose
    val status: Boolean = false,
    @SerializedName("price")
    @Expose
    val price: String  = "",
    @SerializedName("category_breadcrumb")
    @Expose
    val categoryBreadcrumb: String = "",
    @SerializedName("rating")
    @Expose
    val rating: Int  = -1,
    @SerializedName("review_count")
    @Expose
    val reviewCount: Int  = -1,
    @SerializedName("minimum_order")
    @Expose
    val minimumOrder: Int = -1,
    @SerializedName("wholesale_price")
    @Expose
    val wholesalePrice: List<WholesalePrice> = listOf(),
    @SerializedName("shop")
    @Expose
    val shop: Shop = Shop(),
    @SerializedName("preorder")
    @Expose
    val preorder: Boolean = false,
    @SerializedName("badges")
    @Expose
    val badges: List<Badge> = listOf(),
    @SerializedName("label_group")
    @Expose
    val labels: List<LabelGroup> = listOf(),
    @SerializedName("free_ongkir")
    @Expose
    val freeOngkir: FreeOngkir = FreeOngkir()
) : ImpressHolder(){
    override fun equals(other: Any?): Boolean {
        return other is WishlistItem &&
                id == other.id && name == other.name && imageUrl == other.imageUrl && url == other.url && rawPrice == other.rawPrice &&
                condition == other.condition && available == other.available && status == other.status && price == other.price &&
                minimumOrder == other.minimumOrder && wholesalePrice == other.wholesalePrice && shop == other.shop && preorder == other.preorder &&
                badges.count() == badges.count() && labels.count() == labels.count() && freeOngkir == freeOngkir
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + imageUrl.hashCode()
        result = 31 * result + rawPrice
        result = 31 * result + condition.hashCode()
        result = 31 * result + available.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + price.hashCode()
        result = 31 * result + minimumOrder
        result = 31 * result + wholesalePrice.hashCode()
        result = 31 * result + shop.hashCode()
        result = 31 * result + preorder.hashCode()
        result = 31 * result + badges.hashCode()
        result = 31 * result + labels.hashCode()
        result = 31 * result + freeOngkir.hashCode()
        return result
    }
}

data class LabelGroup(
    @SerializedName("title")
    @Expose
    val title: String,
    @SerializedName("type")
    @Expose
    val type: String,
    @SerializedName("position")
    @Expose
    val position: String
)

data class Shop (
    @SerializedName("id")
    @Expose
    val id: String = "",
    @SerializedName("name")
    @Expose
    val name: String = "",
    @SerializedName("url")
    @Expose
    val url: String = "",
    @SerializedName("gold_merchant")
    @Expose
    val goldMerchant: Boolean = false,
    @SerializedName("official_store")
    @Expose
    val officialStore: Boolean = false,
    @SerializedName("status")
    @Expose
    val status: String = "",
    @SerializedName("location")
    @Expose
    val location: String = ""
){
    override fun equals(other: Any?): Boolean {
        return other is Shop && id == other.id && name == other.name && other.url == url &&
                goldMerchant == other.goldMerchant && officialStore == other.officialStore && status == other.status &&
                location == other.location
    }
}

data class WholesalePrice (
    @SerializedName("minimum")
    @Expose
    val minimum: Integer,
    @SerializedName("maximum")
    @Expose
    val maximum: Integer,
    @SerializedName("price")
    @Expose
    val price: Integer
)

data class Wishlist (
    @SerializedName("has_next_page")
    @Expose
    val hasNextPage: Boolean,
    @SerializedName("total_data")
    @Expose
    val totalData: Integer,
    @SerializedName("items")
    @Expose
    val items: List<WishlistItem> = listOf()
)
