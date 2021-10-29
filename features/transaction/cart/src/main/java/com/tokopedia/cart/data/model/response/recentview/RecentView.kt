package com.tokopedia.cart.data.model.response.recentview

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

/**
 * Created by Irfan Khoirul on 21/09/18.
 */

data class RecentView(
    @SerializedName("product_id")
    @Expose
    var productId: String? = null,
    @SerializedName("product_name")
    @Expose
    var productName: String? = null,
    @SerializedName("shop_url")
    @Expose
    var shopUrl: String? = null,
    @SerializedName("product_rating")
    @Expose
    var productRating: Int = 0,
    @SerializedName("product_review_count")
    @Expose
    var productReviewCount: Int = 0,
    @SerializedName("product_image")
    @Expose
    var productImage: String? = null,
    @SerializedName("shop_id")
    @Expose
    var shopId: String? = null,
    @SerializedName("shop_name")
    @Expose
    var shopName: String? = null,
    @SerializedName("product_price")
    @Expose
    var productPrice: String? = null,
    @SerializedName("wishlist")
    @Expose
    var isWishlist: Boolean = false,
    @SerializedName("shop_location")
    @Expose
    var shopLocation: String? = null,
    @SerializedName("badges")
    @Expose
    var badges: List<Badge> = ArrayList()
)