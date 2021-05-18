package com.tokopedia.home_wishlist.model.entity

import android.annotation.SuppressLint
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
        result = HASH_CODE * result + imageUrl.hashCode()
        return result
    }

    companion object{
        private const val HASH_CODE = 31
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
    @SuppressLint("Invalid Data Type")
    val rawPrice: String = "",
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
    @SuppressLint("Invalid Data Type")
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
    @SerializedName("discount_percentage")
    @Expose
    val discountPercentage: Int = 0,
    @SerializedName("slash_price")
    @Expose
    @SuppressLint("Invalid Data Type")
    val slashPrice: String = "",
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
    val freeOngkir: FreeOngkir = FreeOngkir(),
    @SerializedName("free_ongkir_extra")
    @Expose
    val freeOngkirExtra: FreeOngkir = FreeOngkir()
) : ImpressHolder(){
    override fun equals(other: Any?): Boolean {
        return other is WishlistItem &&
                id == other.id && name == other.name && imageUrl == other.imageUrl && url == other.url && rawPrice == other.rawPrice &&
                condition == other.condition && available == other.available && status == other.status && price == other.price &&
                minimumOrder == other.minimumOrder && shop == other.shop && preorder == other.preorder &&
                badges.count() == badges.count() && labels.count() == labels.count() && freeOngkir == freeOngkir && freeOngkirExtra == freeOngkirExtra
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = HASH_CODE * result + name.hashCode()
        result = HASH_CODE * result + url.hashCode()
        result = HASH_CODE * result + imageUrl.hashCode()
        result = HASH_CODE * result + rawPrice.hashCode()
        result = HASH_CODE * result + condition.hashCode()
        result = HASH_CODE * result + available.hashCode()
        result = HASH_CODE * result + status.hashCode()
        result = HASH_CODE * result + price.hashCode()
        result = HASH_CODE * result + minimumOrder
        result = HASH_CODE * result + shop.hashCode()
        result = HASH_CODE * result + preorder.hashCode()
        result = HASH_CODE * result + badges.hashCode()
        result = HASH_CODE * result + labels.hashCode()
        result = HASH_CODE * result + freeOngkir.hashCode()
        result = HASH_CODE * result + freeOngkirExtra.hashCode()
        return result
    }

    companion object{
        private const val HASH_CODE = 31
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
