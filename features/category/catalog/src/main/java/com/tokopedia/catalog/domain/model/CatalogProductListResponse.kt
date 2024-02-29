package com.tokopedia.catalog.domain.model


import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class CatalogProductListResponse(
    @SerializedName("catalogGetProductList")
    val catalogGetProductList: CatalogGetProductList
) {
    data class CatalogGetProductList(
        @SerializedName("header")
        val header: Header,
        @SerializedName("products")
        val products: List<CatalogProduct>
    ) {
        data class Header(
            @SerializedName("totalData")
            val totalData: Int
        )

        data class CatalogProduct(
            @SerializedName("additionalService")
            val additionalService: AdditionalService?,
            @SerializedName("credibility")
            val credibility: Credibility?,
            @SerializedName("delivery")
            val delivery: Delivery?,
            @SerializedName("isVariant")
            val isVariant: Boolean?,
            @SerializedName("labelGroups")
            val labelGroups: List<LabelGroup>?,
            @SerializedName("mediaUrl")
            val mediaUrl: MediaUrl?,
            @SerializedName("paymentOption")
            val paymentOption: PaymentOption?,
            @SuppressLint("Invalid Data Type")
            @SerializedName("price")
            val price: Price?,
            @SerializedName("productID")
            val productID: String?,
            @SerializedName("shop")
            val shop: Shop?,
            @SerializedName("stock")
            val stock: Stock?,
            @SerializedName("warehouseID")
            val warehouseID: String?
        ) {
            data class AdditionalService(
                @SerializedName("name")
                val name: String?
            )

            data class Credibility(
                @SerializedName("rating")
                val rating: String?,
                @SerializedName("ratingCount")
                val ratingCount: String?,
                @SerializedName("sold")
                val sold: String?
            )

            data class Delivery(
                @SerializedName("eta")
                val eta: String?,
                @SerializedName("type")
                val type: String?
            )

            data class LabelGroup(
                @SerializedName("position")
                val position: String?,
                @SerializedName("title")
                val title: String?,
                @SerializedName("url")
                val url: String?
            )

            data class MediaUrl(
                @SerializedName("image")
                val image: String?,
                @SerializedName("image300")
                val image300: String?,
                @SerializedName("image500")
                val image500: String?,
                @SerializedName("image700")
                val image700: String?
            )

            data class PaymentOption(
                @SerializedName("desc")
                val desc: String?,
                @SerializedName("iconUrl")
                val iconUrl: String?
            )

            data class Price(
                @SerializedName("original")
                val original: String?,
                @SerializedName("text")
                val text: String?
            )

            data class Shop(
                @SerializedName("badge")
                val badge: String?,
                @SerializedName("city")
                val city: String?,
                @SerializedName("id")
                val id: String?,
                @SerializedName("name")
                val name: String?
            )

            data class Stock(
                @SerializedName("isHidden")
                val isHidden: Boolean?,
                @SerializedName("soldPercentage")
                val soldPercentage: Int?,
                @SerializedName("wording")
                val wording: String?
            )
        }
    }
}
