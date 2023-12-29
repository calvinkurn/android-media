package com.tokopedia.play.broadcaster.domain.model.campaign

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

/**
 * Created By : Jonathan Darwin on February 08, 2022
 */
data class GetProductTagSummarySectionResponse(
    @SerializedName("broadcasterGetProductTagSection")
    val broadcasterGetProductTagSection: GetProductTagSection = GetProductTagSection()
) {

    data class GetProductTagSection(
        @SerializedName("sections")
        val sections: List<ProductTagSection> = emptyList()
    )

    data class ProductTagSection(
        @SerializedName("name")
        val name: String = "",

        @SerializedName("statusFmt")
        val statusFmt: String = "",

        @SerializedName("products")
        val products: List<ProductList> = emptyList(),
    )

    data class ProductList(
        @SerializedName("productID")
        val productID: String = "",

        @SerializedName("productName")
        val productName: String = "",

        @SerializedName("hasCommission")
        val hasCommission: Boolean = false,

        @SerializedName("commissionFmt")
        val commissionFmt: String = "",

        @SerializedName("commission")
        val commission: Long = 0,

        @SerializedName("extraCommission")
        val extraCommission: Boolean = false,

        @SerializedName("imageURL")
        val imageURL: String = "",

        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        val price: String = "",

        @SerializedName("priceFmt")
        val priceFmt: String = "",

        @SerializedName("discount")
        val discount: String = "",

        @SerializedName("originalPrice")
        val originalPrice: String = "",

        @SerializedName("originalPriceFmt")
        val originalPriceFmt: String = "",

        @SerializedName("quantity")
        val quantity: Int = 0,

        @SerializedName("is_pinned")
        val isPinned: Boolean = false,

        @SerializedName("is_pinnable")
        val isPinnable: Boolean = false,

        @SerializedName("product_number")
        val productNumber: Int = 0,
    )
}
