package com.tokopedia.oldcatalog.model.raw

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName

data class LayoutData(
    @SerializedName("style")
    val style: Style = Style(),
    @SerializedName("hero")
    val hero: Hero = Hero(),
    @SerializedName("topFeature")
    val topFeature: List<TopFeature> = listOf(),
    @SerializedName("trustmaker")
    val trustmaker: List<Trustmaker> = listOf(),
    @SerializedName("characteristic")
    val characteristic: List<Characteristic> = listOf(),
    @SerializedName("singleBanner")
    val singleBanner: Banner = Banner(),
    @SerializedName("doubleBanner")
    val doubleBanner: List<Banner> = listOf(),
    @SerializedName("imagePanel")
    val imagePanel: List<ImagePanel> = listOf(),
    @SerializedName("navigation")
    val navigation: List<Navigation> = listOf(),
    @SerializedName("imageSlider")
    val imageSlider: List<ImageSlider> = listOf(),
    @SerializedName("text")
    val text: Text = Text(),
    @SerializedName("expertReview")
    val expertReview: List<ExpertReview> = listOf(),
    @SerializedName("section")
    val section: Section = Section(),
    @SerializedName("infoColumn")
    val infoColumn: List<InfoColumn> = listOf(),
    @SerializedName("comparison")
    val comparison: List<Comparison> = listOf(),
    @SerializedName("similar")
    val similar: List<Similar> = listOf(),
    @SerializedName("supportFeature")
    val supportFeature: List<SupportFeature> = listOf(),
    @SerializedName("accordion")
    val accordion: List<Accordion> = listOf(),
    @SerializedName("buyerReviewList")
    val buyerReviewList: List<BuyerReviewList> = listOf(),
    @SerializedName("basicInfo")
    val priceCta: BasicInfo = BasicInfo(),
    @SerializedName("searchOverride")
    val searchOverride: List<SearchOverride> = listOf(),
    @SerializedName("video")
    val video: List<CatalogVideo> = listOf(),
    @SerializedName("topSeller")
    val topSeller: TopSeller = TopSeller()
) {

    data class Style(
        @SerializedName("isHidden")
        val isHidden: Boolean = true,
        @SerializedName("isPremium")
        val isPremium: Boolean = false,
        @SerializedName("columnType")
        val columnType: String = "",
        @SerializedName("bannerRatio")
        val bannerRatio: String = "",
        @SerializedName("maxDisplay")
        val maxDisplay: Int = 0,
        @SerializedName("isSticky")
        val isSticky: Boolean = false
    )

    data class Hero(
        @SerializedName("name") var name: String = "",
        @SerializedName("brandLogoUrl") var brandLogoUrl: String = "",
        @SerializedName("heroSlide") var heroSlide: List<HeroSlide> = listOf()
    ) {
        data class HeroSlide(
            @SerializedName("videoUrl") var videoUrl: String = "",
            @SerializedName("imageUrl") var imageUrl: String = "",
            @SerializedName("subtitle") var subtitle: String = ""
        )
    }

    data class TopFeature(
        @SerializedName("iconUrl") var iconUrl: String = "",
        @SerializedName("desc") var desc: String = ""
    )

    data class Trustmaker(
        @SerializedName("imageUrl") var imageUrl: String = "",
        @SerializedName("title") var title: String = "",
        @SerializedName("subtitle") var subtitle: String = ""
    )

    data class Characteristic(
        @SerializedName("iconUrl") var iconUrl: String = "",
        @SerializedName("desc") var desc: String = ""
    )

    data class Banner(
        @SerializedName("imageUrl") var imageUrl: String = ""
    )

    data class ImagePanel(
        @SerializedName("imageUrl") var imageUrl: String = "",
        @SerializedName("title") var title: String = "",
        @SerializedName("subtitle") var subtitle: String = "",
        @SerializedName("desc") var desc: String = ""
    )

    data class Navigation(
        @SerializedName("title") var title: String = "",
        @SerializedName("eligibleNames") var eligibleNames: List<String> = listOf()
    )

    data class ImageSlider(
        @SerializedName("imageUrl") var imageUrl: String = "",
        @SerializedName("title") var title: String = "",
        @SerializedName("subtitle") var subtitle: String = "",
        @SerializedName("desc") var desc: String = ""
    )

    data class Text(
        @SerializedName("title") var title: String = "",
        @SerializedName("subtitle") var subtitle: String = "",
        @SerializedName("desc") var desc: String = ""
    )

    data class ExpertReview(
        @SerializedName("name") var name: String = "",
        @SerializedName("title") var title: String = "",
        @SerializedName("imageUrl") var imageUrl: String = "",
        @SerializedName("review") var review: String = "",
        @SerializedName("videoUrl") var videoUrl: String = ""
    )

    data class Section(
        @SerializedName("title") var title: String = ""
    )

    data class InfoColumn(
        @SerializedName("name") var name: String = "",
        @SerializedName("row") var row: List<Row> = listOf()
    ) {
        data class Row(
            @SerializedName("key") var key: String = "",
            @SerializedName("value") var value: String = ""
        )
    }

    data class Comparison(
        @SerializedName("id") var id: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("url") var url: String = "",
        @SerializedName("mobileUrl") var mobileUrl: String = "",
        @SerializedName("applink") var applink: String = "",
        @SerializedName("catalogImage") var catalogImage: List<CatalogImage> = listOf(),
        @SerializedName("marketPrice") var marketPrice: List<MarketPrice> = listOf(),
        @SerializedName("fullSpec") var fullSpec: List<FullSpec> = listOf(),
        @SerializedName("topSpec") var topSpec: List<TopSpec> = listOf()
    ) {
        data class CatalogImage(
            @SerializedName("imageUrl") var imageUrl: String = "",
            @SerializedName("isPrimary") var isPrimary: Boolean = false
        )

        data class MarketPrice(
            @SerializedName("min") var min: Int = 0,
            @SerializedName("max") var max: Int = 0,
            @SerializedName("minFmt") var minFmt: String = "",
            @SerializedName("maxFmt") var maxFmt: String = "",
            @SerializedName("date") var date: String = "",
            @SerializedName("name") var name: String = ""
        )

        data class Row(
            @SerializedName("key") var key: String = "",
            @SerializedName("value") var value: String = "",
            @SerializedName("flags") var flags: ArrayList<String> = arrayListOf()
        )

        data class FullSpec(
            @SerializedName("name") var name: String = "",
            @SerializedName("icon") var icon: String = "",
            @SerializedName("row") var row: List<Row> = listOf()
        )

        data class TopSpec(
            @SerializedName("key") var key: String = "",
            @SerializedName("value") var value: String = "",
            @SerializedName("icon") var icon: String = ""
        )
    }

    data class Similar(
        @SerializedName("id") var id: String = "",
        @SerializedName("name") var name: String = "",
        @SerializedName("url") var url: String = "",
        @SerializedName("mobileUrl") var mobileUrl: String = "",
        @SerializedName("applink") var applink: String = "",
        @SerializedName("catalogImage") var catalogImage: List<CatalogImage> = listOf(),
        @SerializedName("marketPrice") var marketPrice: List<MarketPrice> = listOf()
    ) {
        data class CatalogImage(
            @SerializedName("imageUrl") var imageUrl: String = "",
            @SerializedName("isPrimary") var isPrimary: Boolean = false
        )

        data class MarketPrice(
            @SerializedName("min") var min: Int = 0,
            @SerializedName("max") var max: Int = 0,
            @SerializedName("minFmt") var minFmt: String = "",
            @SerializedName("maxFmt") var maxFmt: String = "",
            @SerializedName("date") var date: String = "",
            @SerializedName("name") var name: String = ""
        )
    }

    data class SupportFeature(
        @SerializedName("iconUrl") var iconUrl: String = "",
        @SerializedName("title") var title: String = "",
        @SerializedName("desc") var desc: String = ""
    )

    data class AccordionComponent(
        @SerializedName("title") var title: String = "",
        @SerializedName("desc") var desc: String = ""
    )
    data class Accordion(
        @SerializedName("title") var title: String = "",
        @SerializedName("desc") var desc: String = ""
    )

    data class BuyerReviewList(
        @SerializedName("rating") var rating: Int = 0,
        @SerializedName("informativeScore") var informativeScore: Int = 0,
        @SerializedName("reviewerName") var reviewerName: String = "",
        @SerializedName("reviewDate") var reviewDate: String = "",
        @SerializedName("reviewText") var reviewText: String = "",
        @SerializedName("reviewImageUrl") var reviewImageUrl: List<String> = listOf(),
        @SerializedName("reviewId") var reviewId: Long = 0,
        @SerializedName("productId") var productId: Long = 0,
        @SerializedName("productIdString") var productIdString: String = "",
        @SerializedName("productUrl") var productUrl: String = "",
        @SerializedName("reviewerStamp") var reviewerStamp: String = "",
        @SerializedName("reviewerProfilePicture") var reviewerProfilePicture: String = "",
        @SerializedName("productVariantName") var productVariantName: String = "",
        @SerializedName("shopID") var shopID: String = "",
        @SerializedName("shopName") var shopName: String = "",
        @SerializedName("shopURL") var shopURL: String = "",
        @SerializedName("shopBadge") var shopBadge: String = "",
        @SerializedName("shopBadgeSVG") var shopBadgeSVG: String = "",
        @SerializedName("userStats") var userStats: List<BuyerReviewUserStats> = emptyList(),
        @SerializedName("imageAttachments") var imageAttachments: List<BuyerReviewImageAttachment> = emptyList()
    ) {
        data class BuyerReviewUserStats(
            @SerializedName("key") var key: String = "",
            @SerializedName("count") var count: Int = 0
        )

        data class BuyerReviewImageAttachment(
            @SerializedName("attachmentID") var attachmentId: String = "",
            @SerializedName("thumbnailUrl") var thumbnailUrl: String = "",
            @SerializedName("fullsizeUrl") var fullsizeUrl: String = ""
        )
    }

    data class BasicInfo(
        @SerializedName("name") var name: String = "",
        @SerializedName("productSortingStatus") var productSortingStatus: Int = 0,
        @SerializedName("marketPrice") var marketPrice: List<MarketPrice> = listOf()
    ) {
        data class MarketPrice(
            @SerializedName("min") var min: Int = 0,
            @SerializedName("max") var max: Int = 0,
            @SerializedName("minFmt") var minFmt: String = "",
            @SerializedName("maxFmt") var maxFmt: String = "",
            @SerializedName("date") var date: String = "",
            @SerializedName("name") var name: String = ""
        )
    }

    data class SearchOverride(
        @SerializedName("key") var key: String = "",
        @SerializedName("value") var value: String = ""
    )

    data class CatalogVideo(
        @SerializedName("url") val url: String = "",
        @SerializedName("type") val type: String = "",
        @SerializedName("thumbnail") val thumbnail: String = "",
        @SerializedName("author") val author: String = "",
        @SerializedName("title") val title: String = "",
        @SerializedName("videoId") val videoId: String = ""
    )

    data class TopSeller(
        @SerializedName("productID") val productID: String = "",
        @SerializedName("warehouseID") val warehouseID: String = "",
        @SerializedName("isVariant") val isVariant: Boolean = false,
        @SerializedName("shop") val shop: Shop = Shop(),
        @SuppressLint("Invalid Data Type")
        @SerializedName("price")
        val price: Price = Price(),
        @SerializedName("credibility") val credibility: Credibility = Credibility(),
        @SerializedName("labelGroups") val labelGroups: List<LabelGroup> = emptyList(),
        @SerializedName("mediaUrl") val mediaUrl: MediaUrl = MediaUrl(),
        @SerializedName("stock") val stock: Stock = Stock(),
        @SerializedName("paymentOption") val paymentOption: PaymentOption = PaymentOption(),
        @SerializedName("delivery") val delivery: Delivery = Delivery(),
        @SerializedName("additionalService") val additionalService: AdditionalService = AdditionalService()
    ) {
        data class Shop(
            @SerializedName("id") val id: String = "",
            @SerializedName("name") val name: String = "",
            @SerializedName("city") val city: String = "",
            @SerializedName("badge") val badge: String = "",
            @SerializedName("stats") val stats: Stats = Stats()
        ) {
            data class Stats(
                @SerializedName("chatEta") val chatEta: String = "",
                @SerializedName("orderProcessEta") val orderProcessEta: String = ""
            )
        }
        data class Price(
            @SerializedName("text") val text: String = "",
            @SerializedName("original") val original: String = ""
        )
        data class Credibility(
            @SerializedName("rating") val rating: String = "",
            @SerializedName("ratingCount") val ratingCount: String = "",
            @SerializedName("sold") val sold: String = ""
        )
        data class LabelGroup(
            @SerializedName("position") val position: String = "",
            @SerializedName("title") val title: String = "",
            @SerializedName("url") val url: String = "",
            @SerializedName("styles") val styles: List<Style> = emptyList()
        )
        data class Style(
            @SerializedName("key") val key: String = "",
            @SerializedName("value") val value: String = ""
        )
        data class MediaUrl(
            @SerializedName("image300") val thumbnailUrl: String = ""
        )
        data class Stock(
            @SerializedName("soldPercentage") val soldPercentage: Int = 0,
            @SerializedName("isHidden") val isHidden: Boolean = false,
            @SerializedName("wording") val wording: String = ""
        )
        data class PaymentOption(
            @SerializedName("desc") val desc: String = "",
            @SerializedName("iconUrl") val iconUrl: String = ""
        )
        data class Delivery(
            @SerializedName("eta") val eta: String = ""
        )
        data class AdditionalService(
            @SerializedName("name") val name: String = "",
            @SerializedName("iconUrl") val icon: String = ""
        )
    }
}
