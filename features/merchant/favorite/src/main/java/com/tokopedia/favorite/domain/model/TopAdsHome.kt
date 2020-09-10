package com.tokopedia.favorite.domain.model

import com.google.gson.annotations.SerializedName

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
        var isSelected = false

    }

    class Headline {
        @SerializedName("template_id")
        private val templateId: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("image")
        var image: Image? = null

        @SerializedName("shop")
        var shop: Shop? = null

        @SerializedName("button_text")
        var buttonText: String? = null

        @SerializedName("promoted_text")
        private val promotedText: String? = null

        @SerializedName("description")
        var description: String? = null

        @SerializedName("uri")
        var uri: String? = null

        @SerializedName("badges")
        var badges: List<*>? = null

        class Image {
            @SerializedName("full_url")
            var fullUrl: String? = null

            @SerializedName("full_ecs")
            private val fullEcs: String? = null

        }
    }

    class Shop {
        @SerializedName("id")
        var id: String? = null

        @SerializedName("name")
        var name: String? = null

        @SerializedName("domain")
        var domain: String? = null

        @SerializedName("tagline")
        var tagline: String? = null

        @SerializedName("slogan")
        var slogan: String? = null

        @SerializedName("location")
        var location: String? = null

        @SerializedName("city")
        var city: String? = null

        @SerializedName("gold_shop")
        val isGoldShop = false

        @SerializedName("gold_shop_badge")
        private val goldShopBadge = false

        @SerializedName("shop_is_official")
        private val shopIsOfficial = false

        @SerializedName("image_shop")
        val imageShop: ImageShop? = null

        @SerializedName("product")
        var product: List<Product>? = null

        class ImageShop {
            @SerializedName("cover")
            var cover: String? = null

            @SerializedName("s_url")
            private val sUrl: String? = null

            @SerializedName("xs_url")
            val xsUrl: String? = null

            @SerializedName("cover_ecs")
            val coverEcs: String? = null

            @SerializedName("s_ecs")
            val sEcs: String? = null

            @SerializedName("xs_ecs")
            private val xsEcs: String? = null

        }

        class Product {
            @SerializedName("id")
            var id: String? = null

            @SerializedName("name")
            var name: String? = null

            @SerializedName("price_format")
            private val priceFormat: String? = null

            @SerializedName("applinks")
            var applinks: String? = null

            @SerializedName("image_product")
            private val imageProduct: ImageProduct? = null

            class ImageProduct {
                @SerializedName("product_id")
                var productId: String? = null

                @SerializedName("product_name")
                var productName: String? = null

                @SerializedName("image_url")
                var imageUrl: String? = null

                @SerializedName("image_click_url")
                private val imageClickUrl: String? = null

            }
        }
    }
}
