package com.tokopedia.recentview.data.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

class ProductItem {
    @SerializedName("product_id")
    @Expose
    var id // 1
            : String = ""
    @SerializedName("product_name")
    @Expose
    var name // 2
            : String = ""
    @SerializedName("product_url")
    @Expose
    var productUrl // 2
            : String = ""
    @SerializedName("product_price")
    @Expose
    var price // 3
            : String = ""
    @SerializedName("shop_gold_status")
    @Expose
    var isNewGold :Int = 0
    @SerializedName("shop_name")
    @Expose
    var shop // 5
            : String = ""
    @SerializedName("product_image")
    @Expose
    var imgUri // 6
            : String = ""
    var isGold // this is replace by isNewGold
            : String = ""
    @SerializedName("shop_id")
    @Expose
    var shopId :Int = 0
    @SerializedName("labels")
    @Expose
    var labels: List<Label> = ArrayList()
    @SerializedName("badges")
    @Expose
    var badges: List<Badge> = ArrayList()
    @SerializedName("shop_location")
    var shop_location: String = ""
    @SerializedName("product_rating")
    var rating: String = ""
    @SerializedName("product_review_count")
    var reviewCount: String = ""
    @SerializedName("wishlist")
    @Expose
    var wishlist = false


}