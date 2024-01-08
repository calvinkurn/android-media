package com.tokopedia.shareexperience.domain.model.request.affiliate

import com.google.gson.annotations.SerializedName

data class ShareExAffiliatePageDetailRequest(
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
