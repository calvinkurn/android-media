package com.tokopedia.home.beranda.domain.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Lukas on 08/09/20.
 */

data class DisplayHeadlineAdsEntity(
        @SerializedName("displayAdsV3") val displayAds: DisplayAds = DisplayAds()
){
    data class DisplayAds(
            @SerializedName("data") val data: List<DisplayHeadlineAds> = listOf()
    )
    data class DisplayHeadlineAds(
            @SerializedName("id") val id: String = "",
            @SerializedName("ad_ref_key") val adRefKey: String = "",
            @SerializedName("ad_click_url") val adClickUrl: String = "",
            @SerializedName("applinks") val applink: String = "",
            @SerializedName("redirect") val redirect: String = "",
            @SerializedName("headline") val headline: Headline = Headline()
    ){
        data class Headline(
                @SerializedName("button_text") val buttonText: String = "",
                @SerializedName("description") val description: String = "",
                @SerializedName("name") val name: String = "",
                @SerializedName("promoted_text") val promotedText: String = "",
                @SerializedName("template_id") val templateId: String = "",
                @SerializedName("uri") val uri: String = "",
                @SerializedName("image") val image: Image = Image(),
                @SerializedName("shop") val shop: Shop = Shop(),
                @SerializedName("badges") val badges: List<Badges> = listOf()
        ){
            data class Badges(
                    @SerializedName("image_url") val imageUrl: String = "",
                    @SerializedName("title") val title: String = "",
                    @SerializedName("show") val show: Boolean = false
            )
        }

        data class Image(
                @SerializedName("full_url") val url: String = ""
        )

        data class Shop(
                @SerializedName("id") val id: String = "",
                @SerializedName("name") val name: String = "",
                @SerializedName("domain") val domain: String = "",
                @SerializedName("tagline") val tagline: String = "",
                @SerializedName("slogan") val slogan: String = "",
                @SerializedName("location") val location: String = "",
                @SerializedName("city") val city: String = "",
                @SerializedName("gold_shop") val goldShop: Boolean = false,
                @SerializedName("gold_shop_badge") val goldShopBadge: Boolean = false,
                @SerializedName("shop_is_official") val shopIsOfficialStore: Boolean = false,
                @SerializedName("is_followed") val isFollowed: Boolean = false,
                @SerializedName("image_shop") val imageShop: ImageShop = ImageShop(),
                @SerializedName("product") val products: List<Product> = listOf()

        ){
            data class ImageShop(
                    @SerializedName("s_url") val cover: String = ""
            )
        }

        data class Product(
                @SerializedName("id") val id: String = "",
                @SerializedName("name") val name: String = "",
                @SerializedName("price_format") val priceFormat: String = "",
                @SerializedName("uri") val uri: String = "",
                @SerializedName("applink") val applink: String = "",
                @SerializedName("image_product") val imageProduct: ImageProduct = ImageProduct(),
                @SerializedName("product_rating") val rating: Int = 0,
                @SerializedName("count_review_format") val review: String = ""
        ){
            data class ImageProduct(
                    @SerializedName("image_url") val imageUrl: String = "",
                    @SerializedName("image_click_url") val imageClickUrl: String = ""
            )
        }
    }
}
