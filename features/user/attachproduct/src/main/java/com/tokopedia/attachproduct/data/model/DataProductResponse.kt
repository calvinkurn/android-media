package com.tokopedia.attachproduct.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DataProductResponse (
    @SerializedName("url")
    var productUrl: String = "",

    @SerializedName("name")
    var productName: String = "",

    @SerializedName("id")
    var productId: String = "",

    @SerializedName("imageURL700")
    var productImageFull: String = "",

    @SerializedName("imageURL")
    @Expose
    var productImage: String = "",

    @SuppressLint("Invalid Data Type")
    @SerializedName("price")
    var productPrice: String = "",

    @SerializedName("priceInt")
    var priceInt: Int = 0,

    @SerializedName("shop")
    val shop: DataShopResponse = DataShopResponse(),

    @SerializedName("originalPrice")
    val originalPrice: String = "",

    @SerializedName("discountPercentage")
    val discountPercentage: String = "",

    @SerializedName("freeOngkir")
    val freeOngkir: FreeOngkir = FreeOngkir(),

    @SerializedName("stock")
    val stock: Int = 1,

    @SerializedName("childs")
    val childs: List<Int> = listOf(),

    @SerializedName("isPreorder")
    val isPreorder: Boolean = false,

    @SerializedName("categoryID")
    val categoryId: Long = 0

)
