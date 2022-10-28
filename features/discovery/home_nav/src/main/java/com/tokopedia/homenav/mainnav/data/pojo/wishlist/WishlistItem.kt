package com.tokopedia.homenav.mainnav.data.pojo.wishlist

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WishlistItem (
    @SerializedName("id")
    @Expose
    val productId: String? = "",
    @SerializedName("name")
    @Expose
    val name: String? = "",
    @SerializedName("url")
    @Expose
    val url: String? = "",
    @SerializedName("image_url")
    @Expose
    val imageUrl: String? = "",
    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    @Expose
    val price: String? = "0",
    @SerializedName("price_fmt")
    @Expose
    val priceFmt: String? = "",
    @SerializedName("available")
    @Expose
    val isAvailable: Boolean? = false,
    @SerializedName("label_status")
    @Expose
    val labelStatus: String? = "",
    @SerializedName("preorder")
    @Expose
    val predorder: Boolean? = false,
    @SerializedName("rating")
    @Expose
    val rating: String? = "",
    @SerializedName("sold_count")
    @Expose
    val soldCount: String? = "",
    @SerializedName("min_order")
    @Expose
    val minOrder: String? = "0",
    @SerializedName("shop")
    @Expose
    val shop: Shop? = Shop(),
    @SerializedName("badge")
    @Expose
    val badges: List<Badge>? = listOf(),
    @SerializedName("original_price")
    @Expose
    val originalPrice: String? = "0",
    @SerializedName("original_price_fmt")
    @Expose
    val originalPriceFmt: String? = "",
    @SerializedName("discount_percentage")
    @Expose
    val discountPercentage: Int? = 0,
    @SerializedName("discount_percentage_fmt")
    @Expose
    val discountPercentageFmt: String? = "",
    @SerializedName("label_stock")
    @Expose
    val labelStock: String? = "",
    @SerializedName("bebas_ongkir")
    @Expose
    val freeOngkir: FreeOngkir? = FreeOngkir(),
    @SerializedName("wishlist_id")
    @Expose
    val wishlistId: String? = "",
    @SerializedName("variant_name")
    @Expose
    val variant: String? = "",
    @SerializedName("category")
    @Expose
    val category: List<Category>? = listOf(),
    @SerializedName("label_group")
    val labelGroup: List<LabelGroup>? = listOf()
)

data class FreeOngkir (
    @SerializedName("type")
    @Expose
    val type: Int? = 0,
    @SerializedName("image_url")
    @Expose
    val imageUrl: String? = ""
)

data class Category(
    @SuppressLint("Invalid Data Type")
    @SerializedName("category_id")
    @Expose
    val id: Int? = 0,
    @SerializedName("category_name")
    @Expose
    val name: String? = ""
)

data class LabelGroup(
    @SerializedName("title")
    val title: String
)