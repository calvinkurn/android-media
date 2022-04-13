package com.tokopedia.product.detail.data.model.affiliate

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AffiliateCookieRequest(
        @SerializedName("Header")
        @Expose
        val header: AffiliateRequestHeader = AffiliateRequestHeader(),

        @SerializedName("AffiliateDetail")
        @Expose
        val affiliateDetail: AffiliateRequestDetail = AffiliateRequestDetail(),

        @SerializedName("ProductDetail")
        @Expose
        val affiliateProductDetail: AffiliateProductDetail = AffiliateProductDetail(),

        @SerializedName("ShopDetail")
        @Expose
        val affiliateShopDetail: AffiliateShopDetail = AffiliateShopDetail(),

        @SerializedName("LinkDetail")
        @Expose
        val affiliateLinkDetail: AffiliateLinkDetail = AffiliateLinkDetail(),

        @SerializedName("PlatformDetail")
        @Expose
        val affiliatePlatformDetail: AffiliatePlatformDetail = AffiliatePlatformDetail()
)

data class AffiliatePlatformDetail(
        @SerializedName("Platform")
        @Expose
        val platform: String = "",

        @SerializedName("Version")
        @Expose
        val version: String = "",
)

data class AffiliateLinkDetail(
        @SerializedName("AffiliateLink")
        @Expose
        val affiliateLink: String = "",

        @SerializedName("Channel")
        @Expose
        val channel: String = "",

        @SerializedName("LinkType")
        @Expose
        val linkType: String = "",

        @SerializedName("LinkIdentifier")
        @Expose
        val linkIdentifier: String = "",
)

data class AffiliateShopDetail(
        @SerializedName("ShopID")
        @Expose
        val shopId: String = "",
)

data class AffiliateProductDetail(
        @SerializedName("ProductID")
        @Expose
        val productId: String = "",

        @SerializedName("CategoryID")
        @Expose
        val categoryId: String = "",

        @SerializedName("IsVariant")
        @Expose
        val isVariant: Boolean = false,

        @SerializedName("StockQty")
        @Expose
        val stockQty: Double = 0.0
)

data class AffiliateRequestDetail(
        @SerializedName("AffiliateUUID")
        @Expose
        val affiliateUniqueId: String = ""
)

data class AffiliateRequestHeader(
        @SerializedName("TrackerID")
        @Expose
        val uuid: String = "",

        @SerializedName("VisitorID")
        @Expose
        val deviceId: String = "",

        @SerializedName("IrisSessionID")
        @Expose
        val irisSessionId: String = ""
)