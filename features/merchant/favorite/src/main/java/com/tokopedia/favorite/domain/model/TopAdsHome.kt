package com.tokopedia.favorite.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.topads.sdk.domain.model.ImageShop

class TopAdsHome {
    @SerializedName("data")
    var data: List<Data>? = null

    class Data {
        @SerializedName("id")
        var id: String? = null

        @SerializedName("ad_ref_key")
        val adRefKey: String? = null

        @SerializedName("redirect")
        var redirect: String? = null

        @SerializedName("ad_click_url")
        val adClickUrl: String? = null

        @SerializedName("headline")
        var headline: Headline? = null

        @SerializedName("applinks")
        var applinks: String? = null

    }

    class Headline {
        @SerializedName("name")
        var name: String? = null

        @SerializedName("image")
        var image: Image? = null

        @SerializedName("shop")
        var shop: Shop? = null

        @SerializedName("layout")
        var layout: Int? = null

        class Image {
            @SerializedName("full_url")
            var fullUrl: String? = null

            @SerializedName("full_ecs")
            val fullEcs: String? = null

        }
    }

    class Shop {
        @SerializedName("id")
        var id: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("domain")
        var domain: String? = null

        @SerializedName("location")
        var location: String? = null

        @SerializedName("shop_is_official")
        val shopIsOfficial = false

        @SerializedName("pm_pro_shop")
        val isPMPro = false

        @SerializedName("gold_shop")
        val isPowerMerchant = false

        @SerializedName("image_shop")
        val imageShop: ImageShop? = null

        @SerializedName("product")
        var product: List<Product>? = null

        @SerializedName("is_followed")
        var is_followed: Boolean = false

        class Product {
            @SerializedName("id")
            var id: String? = null

            @SerializedName("name")
            var name: String? = null

            @SerializedName("image_product")
            val imageProduct: ImageProduct? = null

            class ImageProduct {
                @SerializedName("image_url")
                var imageUrl: String? = null
            }
        }
    }
}
