package com.tokopedia.home.beranda.presentation.view.adapter.datamodel.static_channel.recommendation

import com.google.gson.annotations.SerializedName

data class AdsBannerResponseModel(
    @SerializedName("Item")
    val item: Item = Item()
) {
    data class Item(
        @SerializedName("AdsBanner")
        val adsBanner: AdsBanner = AdsBanner()
    ) {
        data class AdsBanner(
            @SerializedName("adClickURL")
            val adClickURL: String = "",
            @SerializedName("adRefKey")
            val adRefKey: String = "",
            @SerializedName("adViewURL")
            val adViewURL: String = "",
            @SerializedName("banner")
            val banner: Banner = Banner(),
            @SerializedName("ID")
            val id: String = "0"
        ) {
            data class Banner(
                @SerializedName("bannerImages")
                val bannerImages: List<BannerImage> = listOf(),
                @SerializedName("layoutType")
                val layoutType: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("shop")
                val shop: Shop = Shop()
            ) {
                data class BannerImage(
                    @SerializedName("dimension")
                    val dimension: Dimension = Dimension(),
                    @SerializedName("URL")
                    val uRL: String = ""
                ) {
                    data class Dimension(
                        @SerializedName("height")
                        val height: Int = 0,
                        @SerializedName("ID")
                        val id: String = "0",
                        @SerializedName("width")
                        val width: Int = 0
                    )
                }
                data class Shop(
                    @SerializedName("ID")
                    val id: String = "0",
                    @SerializedName("image")
                    val image: Image = Image(),
                    @SerializedName("name")
                    val name: String = ""
                ) {
                    data class Image(
                        @SerializedName("cover")
                        val cover: String = "",
                        @SerializedName("coverEcs")
                        val coverEcs: String = "",
                        @SerializedName("sEcs")
                        val sEcs: String = "",
                        @SerializedName("sURL")
                        val sURL: String = "",
                        @SerializedName("xsEcs")
                        val xsEcs: String = "",
                        @SerializedName("xsURL")
                        val xsURL: String = ""
                    )
                }
            }
        }
    }
}
