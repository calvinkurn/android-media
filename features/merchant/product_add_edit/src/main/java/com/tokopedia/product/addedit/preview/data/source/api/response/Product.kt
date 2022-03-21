package com.tokopedia.product.addedit.preview.data.source.api.response

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.math.BigInteger

@SuppressLint("Invalid Data Type")
data class Product(
        @SerializedName("productID")
        @Expose
        val productID: String = "",
        @SerializedName("productName")
        @Expose
        val productName: String = "",
        @SerializedName("status")
        @Expose
        val status: String = "",
        @SerializedName("stock")
        @Expose
        val stock: Int = 0,
        @SerializedName("priceCurrency")
        @Expose
        val priceCurrency: String = "",
        @SerializedName("price")
        @Expose
        val price: BigInteger = 0.toBigInteger(),
        @SerializedName("lastUpdatePrice")
        @Expose
        val lastUpdatePrice: String = "",
        @SerializedName("minOrder")
        @Expose
        val minOrder: Int = 0,
        @SerializedName("maxOrder")
        @Expose
        val maxOrder: Int = 0,
        @SerializedName("description")
        @Expose
        val description: String = "",
        @SerializedName("weightUnit")
        @Expose
        val weightUnit: String = "",
        @SerializedName("weight")
        @Expose
        val weight: Int = 0,
        @SerializedName("condition")
        @Expose
        val condition: String = "",
        @SerializedName("mustInsurance")
        @Expose
        val mustInsurance: Boolean = false,
        @SerializedName("sku")
        @Expose
        val sku: String = "",
        @SerializedName("category")
        @Expose
        val category: Category = Category(),
        @SerializedName("menus")
        @Expose
        val menus: List<String> = listOf(),
        @SerializedName("pictures")
        @Expose
        val pictures: List<Picture> = listOf(),
        @SerializedName("preorder")
        @Expose
        val preorder: Preorder = Preorder(),
        @SerializedName("shop")
        @Expose
        val shop: Shop = Shop(),
        @SerializedName("wholesale")
        @Expose
        val wholesales: List<Wholesale> = listOf(),
        @SerializedName("campaign")
        @Expose
        val campaign: Campaign = Campaign(),
        @SerializedName("video")
        @Expose
        val videos: List<Video> = listOf(),
        @SerializedName("cashback")
        @Expose
        val cashback: Cashback = Cashback(),
        @SerializedName("txStats")
        @Expose
        val txStats: TxStats = TxStats(),
        @SerializedName("variant")
        @Expose
        val variant: Variant = Variant()
)