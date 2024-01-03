package com.tokopedia.shareexperience.data.dto.request

import com.google.gson.annotations.SerializedName
import com.tokopedia.linker.utils.AffiliateLinkType
import com.tokopedia.shareexperience.domain.model.ShareExRequest

data class ShareExAffiliateLinkEligibilityRequest(
    @SerializedName("generateAffiliateLinkEligibilityRequest")
    val affiliateEligibilityRequest: ShareExAffiliateEligibilityRequest? = ShareExAffiliateEligibilityRequest()
) : ShareExRequest {
    data class ShareExAffiliateEligibilityRequest(
        @SerializedName("PageType")
        val pageType: String? = "",

        @SerializedName("Product")
        val product: Product? = null,

        @SerializedName("Shop")
        val shop: Shop? = null,

        @SerializedName("PageDetail")
        val pageDetail: PageDetail? = null,

        @Transient
        val affiliateLinkType: AffiliateLinkType? = null
    ) {
        data class Product(
            @SerializedName("ProductID")
            val productID: String? = "",

            @SerializedName("CatLevel1")
            val catLevel1: String? = "",

            @SerializedName("CatLevel2")
            val catLevel2: String? = "",

            @SerializedName("CatLevel3")
            val catLevel3: String? = "",

            @SerializedName("ProductPrice")
            val productPrice: String? = "",

            @SerializedName("MaxProductPrice")
            val maxProductPrice: String? = "",

            @SerializedName("ProductStatus")
            val productStatus: String? = "",

            @SerializedName("FormattedProductPrice")
            val formattedProductPrice: String? = ""
        )

        data class Shop(
            @SerializedName("ShopID")
            val shopID: String? = "",

            @SerializedName("ShopStatus")
            val shopStatus: Int? = 0,

            @SerializedName("IsOS")
            val isOS: Boolean = false,

            @SerializedName("IsPM")
            val isPM: Boolean = false
        )

        data class PageDetail(
            @SerializedName("PageType")
            val pageType: String = "",

            @SerializedName("PageID")
            val pageId: String = "",

            @SerializedName("SiteID")
            val siteId: String = "",

            @SerializedName("VerticalID")
            val verticalId: String = "",

            @SerializedName("PageName")
            val pageName: String = ""
        )
    }
}
