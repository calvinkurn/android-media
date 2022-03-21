package com.tokopedia.homenav.mainnav.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by dhaba
 */
data class AffiliateUserDetailData(
    @Expose
    @SerializedName("affiliateUserDetail")
    val affiliateUserDetail: AffiliateUserDetail = AffiliateUserDetail()
)

data class AffiliateUserDetail(
    @Expose
    @SerializedName("IsRegistered")
    val isRegistered: Boolean = false,
    @Expose
    @SerializedName("Title")
    val title: String = "",
    @Expose
    @SerializedName("Redirection")
    val redirection: Redirection = Redirection()
)

data class Redirection(
    @Expose
    @SerializedName("Android")
    val android: String = ""
)