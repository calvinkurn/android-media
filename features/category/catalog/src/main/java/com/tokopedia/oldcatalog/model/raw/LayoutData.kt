package com.tokopedia.oldcatalog.model.raw

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
    val priceCta: List<BasicInfo> = listOf(),
    @SerializedName("searchOverride")
    val searchOverride: List<SearchOverride> = listOf()
) {

    data class Style(
        @SerializedName("isHidden")
        val isHidden: Boolean = false,
        @SerializedName("isPremium")
        val isPremium: Boolean = false,
        @SerializedName("columnType")
        val columnType: Boolean = false
    )

    data class Hero (
        @SerializedName("name"         ) var name         : String?              = null,
        @SerializedName("brandLogoUrl" ) var brandLogoUrl : String?              = null,
        @SerializedName("heroSlide"    ) var heroSlide    : ArrayList<HeroSlide> = arrayListOf()
    ) {
        data class HeroSlide(
            @SerializedName("videoUrl" ) var videoUrl : String? = null,
            @SerializedName("imageUrl" ) var imageUrl : String? = null,
            @SerializedName("subtitle" ) var subtitle : String? = null
        )
    }

    data class TopFeature (
        @SerializedName("iconUrl" ) var iconUrl : String? = null,
        @SerializedName("desc"    ) var desc    : String? = null
    )

    data class Trustmaker (
        @SerializedName("imageUrl" ) var imageUrl : String? = null,
        @SerializedName("title"    ) var title    : String? = null,
        @SerializedName("subtitle" ) var subtitle : String? = null
    )

    data class Characteristic (
        @SerializedName("iconUrl" ) var iconUrl : String? = null,
        @SerializedName("desc"    ) var desc    : String? = null
    )

    data class Banner (
        @SerializedName("imageUrl" ) var imageUrl : String? = null
    )

    data class ImagePanel (
        @SerializedName("imageUrl" ) var imageUrl : String? = null,
        @SerializedName("title"    ) var title    : String? = null,
        @SerializedName("subtitle" ) var subtitle : String? = null,
        @SerializedName("desc"     ) var desc     : String? = null
    )

    data class Navigation (
        @SerializedName("title"         ) var title         : String?           = null,
        @SerializedName("eligibleNames" ) var eligibleNames : List<String>      = listOf()
    )

    data class ImageSlider (
        @SerializedName("imageUrl" ) var imageUrl : String? = null,
        @SerializedName("title"    ) var title    : String? = null,
        @SerializedName("subtitle" ) var subtitle : String? = null,
        @SerializedName("desc"     ) var desc     : String? = null
    )

    data class Text (
        @SerializedName("title"    ) var title    : String? = null,
        @SerializedName("subtitle" ) var subtitle : String? = null,
        @SerializedName("desc"     ) var desc     : String? = null
    )

    data class ExpertReview (
        @SerializedName("name"     ) var name     : String? = null,
        @SerializedName("title"    ) var title    : String? = null,
        @SerializedName("imageUrl" ) var imageUrl : String? = null,
        @SerializedName("review"   ) var review   : String? = null,
        @SerializedName("videoUrl" ) var videoUrl : String? = null
    )

    data class Section (
        @SerializedName("title"         ) var title         : String?           = null
    )

    data class InfoColumn (
        @SerializedName("name" ) var name : String?        = null,
        @SerializedName("row"  ) var row  : ArrayList<Row> = arrayListOf()
    ) {
        data class Row (
            @SerializedName("key"   ) var key   : String? = null,
            @SerializedName("value" ) var value : String? = null
        )
    }

    data class Comparison (
        @SerializedName("id"           ) var id           : String?                 = null,
        @SerializedName("name"         ) var name         : String?                 = null,
        @SerializedName("url"          ) var url          : String?                 = null,
        @SerializedName("mobileUrl"    ) var mobileUrl    : String?                 = null,
        @SerializedName("applink"      ) var applink      : String?                 = null,
        @SerializedName("catalogImage" ) var catalogImage : ArrayList<CatalogImage> = arrayListOf(),
        @SerializedName("marketPrice"  ) var marketPrice  : ArrayList<MarketPrice>  = arrayListOf(),
        @SerializedName("fullSpec"     ) var fullSpec     : ArrayList<FullSpec>     = arrayListOf(),
        @SerializedName("topSpec"      ) var topSpec      : ArrayList<TopSpec>      = arrayListOf()
    ){
        data class CatalogImage (
            @SerializedName("imageUrl"  ) var imageUrl  : String?  = null,
            @SerializedName("isPrimary" ) var isPrimary : Boolean? = null
        )

        data class MarketPrice (
            @SerializedName("min"    ) var min    : Int?    = null,
            @SerializedName("max"    ) var max    : Int?    = null,
            @SerializedName("minFmt" ) var minFmt : String? = null,
            @SerializedName("maxFmt" ) var maxFmt : String? = null,
            @SerializedName("date"   ) var date   : String? = null,
            @SerializedName("name"   ) var name   : String? = null
        )

        data class Row (
            @SerializedName("key"   ) var key   : String?           = null,
            @SerializedName("value" ) var value : String?           = null,
            @SerializedName("flags" ) var flags : ArrayList<String> = arrayListOf()
        )

        data class FullSpec (
            @SerializedName("name" ) var name : String?        = null,
            @SerializedName("icon" ) var icon : String?        = null,
            @SerializedName("row"  ) var row  : ArrayList<Row> = arrayListOf()
        )

        data class TopSpec (
            @SerializedName("key"   ) var key   : String? = null,
            @SerializedName("value" ) var value : String? = null,
            @SerializedName("icon"  ) var icon  : String? = null
        )
    }

    data class Similar (
        @SerializedName("id"           ) var id           : String?                 = null,
        @SerializedName("name"         ) var name         : String?                 = null,
        @SerializedName("url"          ) var url          : String?                 = null,
        @SerializedName("mobileUrl"    ) var mobileUrl    : String?                 = null,
        @SerializedName("applink"      ) var applink      : String?                 = null,
        @SerializedName("catalogImage" ) var catalogImage : ArrayList<CatalogImage> = arrayListOf(),
        @SerializedName("marketPrice"  ) var marketPrice  : ArrayList<MarketPrice>  = arrayListOf()
    ) {
        data class CatalogImage (
            @SerializedName("imageUrl"  ) var imageUrl  : String?  = null,
            @SerializedName("isPrimary" ) var isPrimary : Boolean? = null
        )

        data class MarketPrice (
            @SerializedName("min"    ) var min    : Int?    = null,
            @SerializedName("max"    ) var max    : Int?    = null,
            @SerializedName("minFmt" ) var minFmt : String? = null,
            @SerializedName("maxFmt" ) var maxFmt : String? = null,
            @SerializedName("date"   ) var date   : String? = null,
            @SerializedName("name"   ) var name   : String? = null
        )
    }

    data class SupportFeature (
        @SerializedName("iconUrl" ) var iconUrl : String? = null,
        @SerializedName("title"   ) var title   : String? = null,
        @SerializedName("desc"    ) var desc    : String? = null
    )

    data class Accordion (
        @SerializedName("title"   ) var title   : String? = null,
        @SerializedName("desc"    ) var desc    : String? = null
    )

    data class BuyerReviewList (
        @SerializedName("rating"                 ) var rating                 : Int?              = null,
        @SerializedName("informativeScore"       ) var informativeScore       : Int?              = null,
        @SerializedName("reviewerName"           ) var reviewerName           : String?           = null,
        @SerializedName("reviewDate"             ) var reviewDate             : String?           = null,
        @SerializedName("reviewText"             ) var reviewText             : String?           = null,
        @SerializedName("reviewImageUrl"         ) var reviewImageUrl         : ArrayList<String> = arrayListOf(),
        @SerializedName("reviewId"               ) var reviewId               : Long?              = null,
        @SerializedName("productId"              ) var productId              : Long?              = null,
        @SerializedName("productIdString"        ) var productIdString        : String?           = null,
        @SerializedName("productUrl"             ) var productUrl             : String?           = null,
        @SerializedName("reviewerStamp"          ) var reviewerStamp          : String?           = null,
        @SerializedName("reviewerProfilePicture" ) var reviewerProfilePicture : String?           = null,
        @SerializedName("productVariantName"     ) var productVariantName     : String?           = null,
        @SerializedName("shopID"                 ) var shopID                 : String?           = null,
        @SerializedName("shopName"               ) var shopName               : String?           = null,
        @SerializedName("shopURL"                ) var shopURL                : String?           = null,
        @SerializedName("shopBadge"              ) var shopBadge              : String?           = null,
        @SerializedName("shopBadgeSVG"           ) var shopBadgeSVG           : String?           = null
    )

    data class BasicInfo (
        @SerializedName("name"                 ) var name                 : String?                = null,
        @SerializedName("productSortingStatus" ) var productSortingStatus : Int?                   = null,
        @SerializedName("marketPrice"          ) var marketPrice          : ArrayList<MarketPrice> = arrayListOf()
    ) {
        data class MarketPrice (
            @SerializedName("min"    ) var min    : Int?    = null,
            @SerializedName("max"    ) var max    : Int?    = null,
            @SerializedName("minFmt" ) var minFmt : String? = null,
            @SerializedName("maxFmt" ) var maxFmt : String? = null,
            @SerializedName("date"   ) var date   : String? = null,
            @SerializedName("name"   ) var name   : String? = null
        )
    }

    data class SearchOverride (
        @SerializedName("key"   ) var key   : String? = null,
        @SerializedName("value" ) var value : String? = null
    )
}
