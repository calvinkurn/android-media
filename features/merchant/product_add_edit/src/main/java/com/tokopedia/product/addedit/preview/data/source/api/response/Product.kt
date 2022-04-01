package com.tokopedia.product.addedit.preview.data.source.api.response

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class Product(
        @SerializedName("productID")
        val productID: String = "",
        @SerializedName("productName")
        val productName: String = "",
        @SerializedName("status")
        val status: String = "",
        @SerializedName("stock")
        val stock: Int = 0,
        @SerializedName("priceCurrency")
        val priceCurrency: String = "",
        @SerializedName("price")
        val price: BigInteger = 0.toBigInteger(),
        @SerializedName("lastUpdatePrice")
        val lastUpdatePrice: String = "",
        @SerializedName("minOrder")
        val minOrder: Int = 0,
        @SerializedName("maxOrder")
        val maxOrder: Int = 0,
        @SerializedName("description")
        val description: String = "",
        @SerializedName("weightUnit")
        val weightUnit: String = "",
        @SerializedName("weight")
        val weight: Int = 0,
        @SerializedName("condition")
        val condition: String = "",
        @SerializedName("mustInsurance")
        val mustInsurance: Boolean = false,
        @SerializedName("sku")
        val sku: String = "",
        @SerializedName("category")
        val category: Category = Category(),
        @SerializedName("menus")
        val menus: List<String> = listOf(),
        @SerializedName("pictures")
        val pictures: List<Picture> = listOf(),
        @SerializedName("preorder")
        val preorder: Preorder = Preorder(),
        @SerializedName("shop")
        val shop: Shop = Shop(),
        @SerializedName("wholesale")
        val wholesales: List<Wholesale> = listOf(),
        @SerializedName("campaign")
        val campaign: Campaign = Campaign(),
        @SerializedName("video")
        val videos: List<Video> = listOf(),
        @SerializedName("cashback")
        val cashback: Cashback = Cashback(),
        @SerializedName("txStats")
        val txStats: TxStats = TxStats(),
        @SerializedName("variant")
        val variant: Variant = Variant()
)