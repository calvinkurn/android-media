package com.tokopedia.shop_showcase.shop_showcase_product_add.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by Rafli Syam 2020-03-09
 */

data class GetProductListData(
        @Expose
        @SerializedName("GetProductList") val getProductList : GetProductList = GetProductList()
)

data class GetProductList (
        @Expose
        @SerializedName("status") val status : String = "",
        @Expose
        @SerializedName("errors") val errors : String = "",
        @Expose
        @SerializedName("totalData") val totalData : Int = 0,
        @Expose
        @SerializedName("links") val links : Links = Links(),
        @Expose
        @SerializedName("data") val data : List<Product> = listOf()
)

data class Product(
        @Expose
        @SerializedName("product_id") val productId : String = "",
        @Expose
        @SerializedName("condition") val productCondition : Int = 0,
        @Expose
        @SerializedName("name") val productName : String = "",
        @Expose
        @SerializedName("product_url") val productUrl : String = "",
        @Expose
        @SerializedName("status") val productStatus : Int = 0,
        @Expose
        @SerializedName("stock") val productStock : Int = 0,
        @Expose
        @SerializedName("minimum_order") val productMinimumOrder : Int = 0,
        @Expose
        @SerializedName("stats") val productStatistic: Statistic = Statistic(),
        @Expose
        @SerializedName("price") val productPrice : Price = Price(),
        @Expose
        @SerializedName("flags") val productFlags : Flags = Flags(),
        @Expose
        @SerializedName("primary_image") val productImage : ProductImage = ProductImage()
)

data class Links (
        @Expose
        @SerializedName("self") val self : String = "",
        @Expose
        @SerializedName("next") val next : String = "",
        @Expose
        @SerializedName("prev") val prev : String = ""
)

data class Price (
        @Expose
        @SerializedName("currency_id") val currencyId : Int = 0,
        @Expose
        @SerializedName("currency_text") val currencyText : String = "",
        @Expose
        @SerializedName("value") val value : Int = 0,
        @Expose
        @SerializedName("value_idr") val valueIdr : Int = 0,
        @Expose
        @SerializedName("text") val text : String = "",
        @Expose
        @SerializedName("text_idr") val textIdr : String = "",
        @Expose
        @SerializedName("identifier") val identifier : String = ""
)

data class Flags (
        @Expose
        @SerializedName("isPreorder") val isPreorder : Boolean = false,
        @Expose
        @SerializedName("isWholesale") val isWholesale : Boolean = false,
        @Expose
        @SerializedName("isWishlist") val isWishlist : Boolean = false,
        @Expose
        @SerializedName("isSold") val isSold : Boolean = false
)

data class ProductImage (
        @Expose
        @SerializedName("original") val original : String = "",
        @Expose
        @SerializedName("thumbnail") val thumbnail : String = ""
)

data class Statistic(
        @Expose
        @SerializedName("reviewCount") val totalReview : Int = 0,
        @Expose
        @SerializedName("rating") val rating : Int = 0
)