package com.tokopedia.home.beranda.domain.gql.recommendationcard

import com.google.gson.annotations.SerializedName

data class AdsBanner(
    @SerializedName("adClickURL")
    val adClickURL: String = "",
    @SerializedName("adRefKey")
    val adRefKey: String = "",
    @SerializedName("adViewURL")
    val adViewURL: String = "",
    @SerializedName("appLink")
    val appLink: String = "",
    @SerializedName("banner")
    val banner: Banner = Banner(),
    @SerializedName("ID")
    val id: String = "0",
    @SerializedName("redirect")
    val redirect: String = ""
) {
    data class Banner(
        @SerializedName("bannerImages")
        val bannerImages: List<BannerImage> = listOf(),
        @SerializedName("layoutType")
        val layoutType: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("position")
        val position: String = "",
        @SerializedName("shop")
        val shop: Shop = Shop()
    )
}

data class BannerImage(
    @SerializedName("dimension")
    val dimension: Dimension = Dimension(),
    @SerializedName("url")
    val url: String = ""
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
    @SerializedName("badge")
    val badge: List<Badge> = listOf(),
    @SerializedName("cityName")
    val cityName: String = "",
    @SerializedName("domain")
    val domain: String = "",
    @SerializedName("ID")
    val id: String = "0",
    @SerializedName("image")
    val image: ShopImage = ShopImage(),
    @SerializedName("isOfficial")
    val isOfficial: Boolean = false,
    @SerializedName("isPowerMerchant")
    val isPowerMerchant: Boolean = false,
    @SerializedName("isPowerMerchantBadge")
    val isPowerMerchantBadge: Boolean = false,
    @SerializedName("name")
    val name: String = "",
    @SerializedName("product")
    val product: List<Product> = listOf(),
    @SerializedName("tagLine")
    val tagLine: String = ""
) {

    data class ShopImage(
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

    data class Badge(
        @SerializedName("imageURL")
        val imageURL: String = "",
        @SerializedName("show")
        val show: Boolean = false,
        @SerializedName("title")
        val title: String = ""
    )

    data class Product(
        @SerializedName("city")
        val city: String = "",
        @SerializedName("discountPercentage")
        val discountPercentage: Int = 0,
        @SerializedName("finalPrice")
        val finalPrice: Int = 0,
        @SerializedName("freeShippingCampaign")
        val freeShippingCampaign: FreeShippingCampaign = FreeShippingCampaign(),
        @SerializedName("ID")
        val id: String = "0",
        @SerializedName("image")
        val image: ImageM = ImageM(),
        @SerializedName("imageProduct")
        val imageProduct: ImageProduct = ImageProduct(),
        @SerializedName("location")
        val location: String = "",
        @SerializedName("name")
        val name: String = "",
        @SerializedName("normalPrice")
        val normalPrice: Int = 0,
        @SerializedName("rating")
        val rating: Int = 0,
        @SerializedName("ratingStar")
        val ratingStar: Int = 0,
        @SerializedName("review")
        val review: Int = 0,
        @SerializedName("URL")
        val url: String = ""
    ) {

        data class ImageM(
            @SerializedName("imageMSqURL")
            val imageMSqURL: String = "",
            @SerializedName("imageMURL")
            val imageMURL: String = ""
        )

        data class FreeShippingCampaign(
            @SerializedName("imgURL")
            val imgURL: String = "",
            @SerializedName("isActive")
            val isActive: Boolean = false
        )

        data class ImageProduct(
            @SerializedName("imageClickURL")
            val imageClickURL: String = "",
            @SerializedName("imageURL")
            val imageURL: String = "",
            @SerializedName("productID")
            val productID: String = "0",
            @SerializedName("productName")
            val productName: String = ""
        )
    }
}
