package com.tokopedia.tokopedianow.common.domain.model

import com.google.gson.annotations.SerializedName

data class GetHomeBannerV2DataResponse(
    @SerializedName("getHomeBannerV2")
    val data: GetHomeBannerV2Response = GetHomeBannerV2Response()
) {

    data class GetHomeBannerV2Response(
        @SerializedName("banners")
        val banners: List<Banners> = arrayListOf()
    )

    data class Banners(
        @SerializedName("id")
        val id: String = "",
        @SerializedName("url")
        val url: String = "",
        @SerializedName("type")
        val type: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("applink")
        val applink: String = "",
        @SerializedName("message")
        val message: String = "",
        @SerializedName("persona")
        val persona: String = "",
        @SerializedName("brandID")
        val brandID: String = "",
        @SerializedName("imageUrl")
        val imageUrl: String = "",
        @SerializedName("startTime")
        val startTime: String = "",
        @SerializedName("promoCode")
        val promoCode: String = "",
        @SerializedName("backColor")
        val backColor: String = "",
        @SerializedName("expireTime")
        val expireTime: String = "",
        @SerializedName("slideIndex")
        val slideIndex: Int? = null,
        @SerializedName("categoryID")
        val categoryID: String = "",
        @SerializedName("creativeName")
        val creativeName: String = "",
        @SerializedName("campaignCode")
        val campaignCode: String = "",
        @SerializedName("topadsViewUrl")
        val topadsViewUrl: String = "",
        @SerializedName("pgCampaignType")
        val pgCampaignType: String = "",
        @SerializedName("categoryPersona")
        val categoryPersona: String = "",
        @SerializedName("galaxyAttribution")
        val galaxyAttribution: String = "",
        @SerializedName("redirectionType")
        val redirectionType: Int? = null,
        @SerializedName("bottomSheetContent")
        val bottomSheetContent: String = ""
    )
}
