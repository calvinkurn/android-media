package com.tokopedia.home_wishlist.model.entity

import android.text.Spanned
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*

class ProductItem : Serializable {

    @SerializedName("product_id")
    @Expose
    var id: String? = null// 1

    @SerializedName("product_name")
    @Expose
    var name: String? = null// 2

    @SerializedName("product_price")
    @Expose
    var price: String? = null// 3

    @SerializedName("shop_gold_status")
    @Expose
    var isNewGold: Int = 0// 4

    @SerializedName("shop_name")
    @Expose
    var shop: String? = null// 5

    @SerializedName("product_image")
    @Expose
    var imgUri: String? = null// 6

    var isGold: String? = null// this is replace by isNewGold

    @SerializedName("shop_lucky")
    @Expose
    var luckyShop: String? = null// 7

    @SerializedName("shop_id")
    @Expose
    var shopId: Int = 0// 8


    @SerializedName("product_preorder")
    @Expose
    var preorder: String? = null


    @SerializedName("product_wholesale")
    var wholesale: String? = null

    @SerializedName("labels")
    @Expose
    var labels: List<Label> = ArrayList()

    /**
     *
     * @return
     * The badges
     */
    /**
     *
     * @param badges
     * The badges
     */
    @SerializedName("badges")
    @Expose
    var badges: List<Badge>? = ArrayList()

    @SerializedName("shop_location")
    var shopLocation: String? = null

    @SerializedName("free_return")
    var free_return: String? = null

    @SerializedName("rating")
    var rating: String? = null

    @SerializedName("review_count")
    var reviewCount: String? = null

    @SerializedName("official_store")
    var isOfficial = false

    var isProductAlreadyWishlist: Boolean = false

    var spannedName: Spanned? = null

    var spannedShop: Spanned? = null

    var isWishlist: Boolean? = false

    var isAvailable: Boolean? = true

    var isTopAds: Boolean? = false

    var trackerListName: String? = null
    var trackerAttribution: String? = null

    var originalPrice: String? = null
    var discountPercentage: Int = 0
    var countCourier: Int = 0
    var cashback: String? = null

    var official: Boolean?
        get() = isOfficial
        set(official) {
            isOfficial = official!!
        }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false

        val that = o as ProductItem?

        if (isNewGold != that!!.isNewGold) return false
        if (shopId != that.shopId) return false
        if (if (id != null) id != that.id else that.id != null) return false
        if (if (name != null) name != that.name else that.name != null) return false
        if (if (price != null) price != that.price else that.price != null) return false
        if (if (shop != null) shop != that.shop else that.shop != null) return false
        if (if (imgUri != null) imgUri != that.imgUri else that.imgUri != null) return false
        return if (if (isGold != null) isGold != that.isGold else that.isGold != null) false else !if (luckyShop != null) luckyShop != that.luckyShop else that.luckyShop != null

    }

    override fun hashCode(): Int {
        var result = if (id != null) id!!.hashCode() else 0
        result = 31 * result + if (name != null) name!!.hashCode() else 0
        result = 31 * result + if (price != null) price!!.hashCode() else 0
        result = 31 * result + isNewGold
        result = 31 * result + if (shop != null) shop!!.hashCode() else 0
        result = 31 * result + if (imgUri != null) imgUri!!.hashCode() else 0
        result = 31 * result + if (isGold != null) isGold!!.hashCode() else 0
        result = 31 * result + if (luckyShop != null) luckyShop!!.hashCode() else 0
        result = 31 * result + shopId
        return result
    }

    override fun toString(): String {
        return "ProductItem{" +
                "id='" + id + '\''.toString() +
                ", name='" + name + '\''.toString() +
                ", price='" + price + '\''.toString() +
                ", shop='" + shop + '\''.toString() +
                ", imgUri='" + imgUri + '\''.toString() +
                ", isGold='" + isGold + '\''.toString() +
                ", luckyShop='" + luckyShop + '\''.toString() +
                '}'.toString()
    }

}