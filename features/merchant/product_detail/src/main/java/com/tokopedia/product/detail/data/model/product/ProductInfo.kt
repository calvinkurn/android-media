package com.tokopedia.product.detail.data.model.product

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProductInfo(
        @SerializedName("basic")
        @Expose
        val basic: Basic = Basic(),

        @SerializedName("brand")
        @Expose
        val brand: Brand = Brand(),

        @SerializedName("campaign")
        @Expose
        val campaign: Campaign = Campaign(),

        @SerializedName("cashback")
        @Expose
        val cashback: Cashback = Cashback(),

        @SerializedName("catalog")
        @Expose
        val catalog: Catalog = Catalog(),

        @SerializedName("category")
        @Expose
        val category: Category = Category(),

        @SerializedName("menu")
        @Expose
        val menu: Menu = Menu(),

        @SerializedName("pictures")
        @Expose
        val pictures: List<Picture> = listOf(),

        @SerializedName("preorder")
        @Expose
        val preorder: PreOrder = PreOrder(),

        @SerializedName("returnInfo")
        @Expose
        val returnInfo: ReturnInfo = ReturnInfo(),

        @SerializedName("stats")
        @Expose
        val stats: Stats = Stats(),

        @SerializedName("txStats")
        @Expose
        val txStats: TxStats = TxStats(),

        @SerializedName("videos")
        @Expose
        val videos: List<Video> = listOf(),

        @SerializedName("wholesale")
        @Expose
        val wholesale: List<Wholesale> = listOf()
) {
        data class Response(
                @SerializedName("getPDPInfo")
                @Expose
                val data: ProductInfo = ProductInfo()
        )

        data class WishlistStatus(
                @SerializedName("ProductWishlistQuery")
                @Expose
                var isWishlisted: Boolean? = null
        )
}