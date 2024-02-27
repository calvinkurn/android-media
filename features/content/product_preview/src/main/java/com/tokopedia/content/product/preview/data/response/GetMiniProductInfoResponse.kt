package com.tokopedia.content.product.preview.data.response

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 27/11/23
 */
data class GetMiniProductInfoResponse(
    @SerializedName("productrevGetMiniProductInfo")
    val data: DataInfo = DataInfo()
) {
    data class DataInfo(
        @SerializedName("product")
        val product: Product = Product(),

        @SerializedName("campaign")
        val campaign: Campaign = Campaign(),

        @SerializedName("shop")
        val shop: Shop = Shop(),

        @SerializedName("hasVariant")
        val hasVariant: Boolean = false,

        @SerializedName("buttonState")
        val buttonState: String = "",

        @SerializedName("categoryTree")
        val categoryTree: List<CategoryTree> = emptyList()
    )

    data class Product(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("priceFmt")
        val priceFmt: String = "",
        @SerializedName("stock")
        val stock: Int = 0,
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        val price: Int = 0,
    )

    data class Campaign(
        @SerializedName("isActive")
        val isActive: Boolean = false,
        @SerializedName("discountPercentage")
        val discountPercentage: Int = 0,
        @SerializedName("discountedPrice")
        val discountedPrice: Double = 0.0
    )

    data class Shop(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("isTokoNow")
        val isTokoNow: Boolean = false
    )

    data class CategoryTree(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("title")
        val title: String = ""
    )
}
