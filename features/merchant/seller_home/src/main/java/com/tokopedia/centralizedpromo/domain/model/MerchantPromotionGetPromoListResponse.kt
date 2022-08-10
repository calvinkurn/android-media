package com.tokopedia.centralizedpromo.domain.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.network.data.model.response.Header

data class MerchantPromotionGetPromoListResponse(
    @Expose
    @SerializedName("merchantPromotionGetPromoList")
    val merchantPromotionGetPromoList: MerchantPromotionGetPromoList = MerchantPromotionGetPromoList()
)

data class MerchantPromotionGetPromoList(
    @Expose
    @SerializedName("header")
    val header: Header = Header(),
    @Expose
    @SerializedName("data")
    val data: MerchantPromotionGetPromoListData = MerchantPromotionGetPromoListData()
)

data class MerchantPromotionGetPromoListData(
    @Expose
    @SerializedName("tab")
    val filterTab: List<FilterTab> = listOf(),
    @Expose
    @SerializedName("pages")
    val pages: List<MerchantPromotionGetPromoListPage> = listOf()
)

data class MerchantPromotionGetPromoListPage(
    @Expose
    @SerializedName("page_id")
    val pageId: String = "",
    @Expose
    @SerializedName("page_name")
    val pageName: String = "",
    @Expose
    @SerializedName("page_name_suffix")
    val pageNameSuffix: String = "",
    @Expose
    @SerializedName("icon_image")
    val iconImage: String = "",
    @Expose
    @SerializedName("banner_image")
    val bannerImage: String = "",
    @Expose
    @SerializedName("not_available_text")
    val notAvailableText: String = "",
    @Expose
    @SerializedName("header_text")
    val headerText: String = "",
    @Expose
    @SerializedName("bottom_text")
    val bottomText: String = "",
    @Expose
    @SerializedName("cta_text")
    val ctaText: String = "",
    @Expose
    @SerializedName("cta_link")
    val ctaLink: String = "",
    @Expose
    @SerializedName("info_text")
    val infoText: String = "",
    @Expose
    @SerializedName("is_eligible")
    val isEligible: Int = 0
) {

    companion object {
        private const val IS_ELIGIBLE = 1
    }

    fun getIsEligible(): Boolean = isEligible == IS_ELIGIBLE
}


data class FilterTab(
    @Expose
    @SerializedName("id")
    val id:String = "",
    @Expose
    @SerializedName("name")
    val name: String = ""
)