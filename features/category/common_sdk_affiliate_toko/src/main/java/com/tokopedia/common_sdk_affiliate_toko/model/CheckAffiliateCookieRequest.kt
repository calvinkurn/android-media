package com.tokopedia.common_sdk_affiliate_toko.model

import com.google.gson.annotations.SerializedName


data class CheckAffiliateCookieRequest(
    @SerializedName("AdditionalParams")
    var additionalParams: List<AdditionalParam?>?,
    @SerializedName("PageDetail")
    var pageDetail: PageDetail?,
    @SerializedName("Header")
    var header: Header?,
) {
    data class AdditionalParam(
        @SerializedName("Key")
        var key: String?,
        @SerializedName("Value")
        var value: String?
    )

    data class Header(
        @SerializedName("VisitorID")
        var visitorID: String?
    )

    data class PageDetail(
        @SerializedName("PageID")
        var pageID: String?,
        @SerializedName("PageType")
        var pageType: String?,
        @SerializedName("SiteID")
        var siteID: String?,
        @SerializedName("VerticalID")
        var verticalID: String?,
        @SerializedName("AffiliateUUID")
        var affiliateUUID: String?
    )

}