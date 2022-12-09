package com.tokopedia.shop.product.data.model

import android.annotation.SuppressLint
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.data.source.cloud.model.FreeOngkir
import com.tokopedia.shop.common.data.source.cloud.model.LabelGroup

data class ShopProduct(
    @SerializedName("campaign")
    @Expose
    val campaign: Campaign = Campaign(),

    @SerializedName("cashback")
    @Expose
    val cashback: CashbackDetail = CashbackDetail(),

    @SerializedName("flags")
    @Expose
    val flags: Flags = Flags(),

    @SerializedName("name")
    @Expose
    val name: String = "",

    @SuppressLint("Invalid Data Type") // cannot use string or double since the response is an object
    @SerializedName("price")
    @Expose
    val price: Price = Price(),

    @SerializedName("primary_image")
    @Expose
    val primaryImage: PrimaryImage = PrimaryImage(),

    @SerializedName("product_id")
    @Expose
    val productId: String = "",

    @SerializedName("product_url")
    @Expose
    val productUrl: String = "",

    @SerializedName("stats")
    @Expose
    val stats: Stats = Stats(),

    @SerializedName("status")
    @Expose
    val status: Int = 0,

    @SerializedName("stock")
    @Expose
    val stock: Int = 0,

    @SerializedName("minimum_order")
    @Expose
    val minimumOrder: Int = 0,

    @SerializedName("max_order")
    @Expose
    val maximumOrder: Int = 0,

    @SerializedName("freeOngkir")
    @Expose
    val freeOngkir: FreeOngkir = FreeOngkir(),

    @SerializedName("label_groups")
    @Expose
    val labelGroupList: List<LabelGroup> = listOf(),

    @SerializedName("hasVariant")
    @Expose
    val hasVariant: Boolean = false,

    @SerializedName("parent_id")
    @Expose
    val parentId: String = "",

    @SerializedName("app_link")
    @Expose
    val appLink: String = ""
) {
    data class Response(
        @SerializedName("GetShopProduct")
        @Expose
        val getShopProduct: GetShopProduct = GetShopProduct()
    )

    data class GetShopProduct(
        @SerializedName("data")
        @Expose
        val `data`: List<ShopProduct> = listOf(),

        @SerializedName("errors")
        @Expose
        val errors: String = "",

        @SerializedName("status")
        @Expose
        val status: String = "",

        @SerializedName("suggestion")
        @Expose
        val suggestion: ShopProductSearchSuggestion = ShopProductSearchSuggestion(),

        @SerializedName("totalData")
        @Expose
        val totalData: Int = 0
    )

    data class ShopProductSearchSuggestion(
        @SerializedName("text")
        @Expose
        val text: String = "",
        @SerializedName("query")
        @Expose
        val query: String = "",
        @SerializedName("response_code")
        @Expose
        val responseCode: String = "",
        @SerializedName("keyword_process")
        @Expose
        val keywordProcess: String = ""
    )

    data class Price(
        @SerializedName("text_idr")
        @Expose
        val textIdr: String = ""
    )

    data class PrimaryImage(
        @SerializedName("original")
        @Expose
        val original: String = "",

        @SerializedName("resize300")
        @Expose
        val resize300: String = "",

        @SerializedName("thumbnail")
        @Expose
        val thumbnail: String = ""
    )

    data class Stats(
        @SerializedName("rating")
        @Expose
        val rating: Int = 0,

        @SerializedName("reviewCount")
        @Expose
        val reviewCount: Int = 0,

        @SerializedName("viewCount")
        @Expose
        val viewCount: Int = 0

    )
}
