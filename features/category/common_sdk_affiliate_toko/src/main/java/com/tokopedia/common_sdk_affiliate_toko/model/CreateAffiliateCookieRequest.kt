package com.tokopedia.common_sdk_affiliate_toko.model

import com.google.gson.annotations.SerializedName


data class CreateAffiliateCookieRequest(
    @SerializedName("AdditionalParams")
    var additionalParams: List<AdditionalParam?>?,
    @SerializedName("AffiliateDetail")
    var affiliateDetail: AffiliateDetail?,
    @SerializedName("CookieLevel")
    var cookieLevel: CookieLevel?,
    @SerializedName("Header")
    var header: Header?,
    @SerializedName("LinkDetail")
    var linkDetail: LinkDetail?,
    @SerializedName("PageDetail")
    var pageDetail: PageDetail?,
    @SerializedName("PlatformDetail")
    var platformDetail: PlatformDetail?,
    @SerializedName("ProductDetail")
    var productDetail: ProductDetail?,
    @SerializedName("ShopDetail")
    var shopDetail: ShopDetail?
) {
    data class AdditionalParam(
        @SerializedName("Key")
        var key: String?,
        @SerializedName("Value")
        var value: String?
    )

    data class AffiliateDetail(
        @SerializedName("AffiliateUUID")
        var affiliateUUID: String?
    )

    data class CookieLevel(
        @SerializedName("level")
        var level: String?
    )

    data class Header(
        @SerializedName("IrisSessionID")
        var irisSessionID: String?,
        @SerializedName("TrackerID")
        var trackerID: String?,
        @SerializedName("VisitorID")
        var visitorID: String?
    )

    data class LinkDetail(
        @SerializedName("AffiliateLink")
        var affiliateLink: String?,
        @SerializedName("Channel")
        var channel: String?,
        @SerializedName("LinkIdentifier")
        var linkIdentifier: String?,
        @SerializedName("LinkType")
        var linkType: String?
    )

    data class PageDetail(
        @SerializedName("PageID")
        var pageID: String?,
        @SerializedName("PageType")
        var pageType: String?,
        @SerializedName("SiteID")
        var siteID: String?,
        @SerializedName("VerticalID")
        var verticalID: String?
    )

    data class PlatformDetail(
        @SerializedName("Platform")
        var platform: String?,
        @SerializedName("Version")
        var version: String?
    )

    data class ProductDetail(
        @SerializedName("CategoryID")
        var categoryID: String?,
        @SerializedName("IsVariant")
        var isVariant: Boolean?,
        @SerializedName("ProductID")
        var productID: String?,
        @SerializedName("StockQty")
        var stockQty: Int?
    )

    data class ShopDetail(
        @SerializedName("ShopID")
        var shopID: String?
    )
}