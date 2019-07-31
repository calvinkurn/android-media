package com.tokopedia.shop.product.data.model


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

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
        val stock: Int = 0
){
        data class Response(
                @SerializedName("GetShopProduct")
                @Expose
                val getShopProduct: GetShopProduct = GetShopProduct())

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

                @SerializedName("totalData")
                @Expose
                val totalData: Int = 0
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
                val reviewCount: Int = 0
        )
}