package com.tokopedia.product.detail.common.data.model.pdplayout


import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.common.data.model.product.*

data class ComponentData(
        @SerializedName("applink")
        val applink: String = "",
        @SerializedName("content")
        val content: List<Content> = listOf(),
        @SerializedName("row")
        val row: String = "",
        @SerializedName("title")
        val title: String = "",

        // Snapshot data
        @SerializedName("campaign")
        val campaign: Campaign = Campaign(),
        @SerializedName("isCOD")
        val isCOD: Boolean = false,
        @SerializedName("isCashback")
        val isCashback: Cashback = Cashback(),
        @SerializedName("isFreeOngkir")
        val isFreeOngkir: IsFreeOngkir = IsFreeOngkir(),
        @SerializedName("isOS")
        val isOS: Boolean = false,
        @SerializedName("isPowerMerchant")
        val isPowerMerchant: Boolean = false,
        @SerializedName("isTradeIn")
        val isTradeIn: Boolean = false,
        @SerializedName("isWishlist")
        val isWishlist: Boolean = false,
        @SerializedName("media")
        val media: List<Media> = listOf(),
        @SerializedName("name")
        val name: String = "",
        @SerializedName("pictures")
        val pictures: List<Picture> = listOf(),
        @SerializedName("price")
        val price: Price = Price(),
        @SerializedName("stock")
        val stock: Stock = Stock(),
        @SerializedName("variant")
        val variant: Variant = Variant(),
        @SerializedName("videos")
        val videos: List<Video> = listOf(),
        @SerializedName("wholesale")
        val wholesale: List<Wholesale> = listOf()

)